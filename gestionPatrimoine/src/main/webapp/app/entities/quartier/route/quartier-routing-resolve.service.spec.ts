jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IQuartier, Quartier } from '../quartier.model';
import { QuartierService } from '../service/quartier.service';

import { QuartierRoutingResolveService } from './quartier-routing-resolve.service';

describe('Service Tests', () => {
  describe('Quartier routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: QuartierRoutingResolveService;
    let service: QuartierService;
    let resultQuartier: IQuartier | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(QuartierRoutingResolveService);
      service = TestBed.inject(QuartierService);
      resultQuartier = undefined;
    });

    describe('resolve', () => {
      it('should return IQuartier returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultQuartier = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultQuartier).toEqual({ id: 123 });
      });

      it('should return new IQuartier if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultQuartier = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultQuartier).toEqual(new Quartier());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultQuartier = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultQuartier).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
