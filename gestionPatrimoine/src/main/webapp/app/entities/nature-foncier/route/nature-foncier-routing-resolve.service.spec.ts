jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { INatureFoncier, NatureFoncier } from '../nature-foncier.model';
import { NatureFoncierService } from '../service/nature-foncier.service';

import { NatureFoncierRoutingResolveService } from './nature-foncier-routing-resolve.service';

describe('Service Tests', () => {
  describe('NatureFoncier routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: NatureFoncierRoutingResolveService;
    let service: NatureFoncierService;
    let resultNatureFoncier: INatureFoncier | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(NatureFoncierRoutingResolveService);
      service = TestBed.inject(NatureFoncierService);
      resultNatureFoncier = undefined;
    });

    describe('resolve', () => {
      it('should return INatureFoncier returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultNatureFoncier = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultNatureFoncier).toEqual({ id: 123 });
      });

      it('should return new INatureFoncier if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultNatureFoncier = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultNatureFoncier).toEqual(new NatureFoncier());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultNatureFoncier = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultNatureFoncier).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
