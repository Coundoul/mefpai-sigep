jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IIntendant, Intendant } from '../intendant.model';
import { IntendantService } from '../service/intendant.service';

import { IntendantRoutingResolveService } from './intendant-routing-resolve.service';

describe('Service Tests', () => {
  describe('Intendant routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: IntendantRoutingResolveService;
    let service: IntendantService;
    let resultIntendant: IIntendant | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(IntendantRoutingResolveService);
      service = TestBed.inject(IntendantService);
      resultIntendant = undefined;
    });

    describe('resolve', () => {
      it('should return IIntendant returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultIntendant = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultIntendant).toEqual({ id: 123 });
      });

      it('should return new IIntendant if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultIntendant = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultIntendant).toEqual(new Intendant());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultIntendant = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultIntendant).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
