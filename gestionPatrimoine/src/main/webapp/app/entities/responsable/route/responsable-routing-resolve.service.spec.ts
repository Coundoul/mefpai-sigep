jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IResponsable, Responsable } from '../responsable.model';
import { ResponsableService } from '../service/responsable.service';

import { ResponsableRoutingResolveService } from './responsable-routing-resolve.service';

describe('Service Tests', () => {
  describe('Responsable routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ResponsableRoutingResolveService;
    let service: ResponsableService;
    let resultResponsable: IResponsable | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ResponsableRoutingResolveService);
      service = TestBed.inject(ResponsableService);
      resultResponsable = undefined;
    });

    describe('resolve', () => {
      it('should return IResponsable returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultResponsable = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultResponsable).toEqual({ id: 123 });
      });

      it('should return new IResponsable if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultResponsable = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultResponsable).toEqual(new Responsable());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultResponsable = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultResponsable).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
