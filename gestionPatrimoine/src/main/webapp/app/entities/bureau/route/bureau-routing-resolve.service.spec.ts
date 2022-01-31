jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IBureau, Bureau } from '../bureau.model';
import { BureauService } from '../service/bureau.service';

import { BureauRoutingResolveService } from './bureau-routing-resolve.service';

describe('Service Tests', () => {
  describe('Bureau routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: BureauRoutingResolveService;
    let service: BureauService;
    let resultBureau: IBureau | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(BureauRoutingResolveService);
      service = TestBed.inject(BureauService);
      resultBureau = undefined;
    });

    describe('resolve', () => {
      it('should return IBureau returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBureau = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultBureau).toEqual({ id: 123 });
      });

      it('should return new IBureau if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBureau = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultBureau).toEqual(new Bureau());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBureau = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultBureau).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
