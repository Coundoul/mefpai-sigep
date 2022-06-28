jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IIntervenant, Intervenant } from '../intervenant.model';
import { IntervenantService } from '../service/intervenant.service';

import { IntervenantRoutingResolveService } from './intervenant-routing-resolve.service';

describe('Service Tests', () => {
  describe('Intervenant routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: IntervenantRoutingResolveService;
    let service: IntervenantService;
    let resultIntervenant: IIntervenant | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(IntervenantRoutingResolveService);
      service = TestBed.inject(IntervenantService);
      resultIntervenant = undefined;
    });

    describe('resolve', () => {
      it('should return IIntervenant returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultIntervenant = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultIntervenant).toEqual({ id: 123 });
      });

      it('should return new IIntervenant if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultIntervenant = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultIntervenant).toEqual(new Intervenant());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultIntervenant = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultIntervenant).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
