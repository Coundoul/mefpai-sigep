jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IBatiment, Batiment } from '../batiment.model';
import { BatimentService } from '../service/batiment.service';

import { BatimentRoutingResolveService } from './batiment-routing-resolve.service';

describe('Service Tests', () => {
  describe('Batiment routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: BatimentRoutingResolveService;
    let service: BatimentService;
    let resultBatiment: IBatiment | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(BatimentRoutingResolveService);
      service = TestBed.inject(BatimentService);
      resultBatiment = undefined;
    });

    describe('resolve', () => {
      it('should return IBatiment returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBatiment = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultBatiment).toEqual({ id: 123 });
      });

      it('should return new IBatiment if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBatiment = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultBatiment).toEqual(new Batiment());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBatiment = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultBatiment).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
