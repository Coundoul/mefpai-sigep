jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IFormateurs, Formateurs } from '../formateurs.model';
import { FormateursService } from '../service/formateurs.service';

import { FormateursRoutingResolveService } from './formateurs-routing-resolve.service';

describe('Service Tests', () => {
  describe('Formateurs routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: FormateursRoutingResolveService;
    let service: FormateursService;
    let resultFormateurs: IFormateurs | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(FormateursRoutingResolveService);
      service = TestBed.inject(FormateursService);
      resultFormateurs = undefined;
    });

    describe('resolve', () => {
      it('should return IFormateurs returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFormateurs = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultFormateurs).toEqual({ id: 123 });
      });

      it('should return new IFormateurs if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFormateurs = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultFormateurs).toEqual(new Formateurs());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFormateurs = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultFormateurs).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
