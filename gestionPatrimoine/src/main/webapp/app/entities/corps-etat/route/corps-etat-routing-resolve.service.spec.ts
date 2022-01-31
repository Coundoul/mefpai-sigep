jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICorpsEtat, CorpsEtat } from '../corps-etat.model';
import { CorpsEtatService } from '../service/corps-etat.service';

import { CorpsEtatRoutingResolveService } from './corps-etat-routing-resolve.service';

describe('Service Tests', () => {
  describe('CorpsEtat routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CorpsEtatRoutingResolveService;
    let service: CorpsEtatService;
    let resultCorpsEtat: ICorpsEtat | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CorpsEtatRoutingResolveService);
      service = TestBed.inject(CorpsEtatService);
      resultCorpsEtat = undefined;
    });

    describe('resolve', () => {
      it('should return ICorpsEtat returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCorpsEtat = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCorpsEtat).toEqual({ id: 123 });
      });

      it('should return new ICorpsEtat if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCorpsEtat = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCorpsEtat).toEqual(new CorpsEtat());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCorpsEtat = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCorpsEtat).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
