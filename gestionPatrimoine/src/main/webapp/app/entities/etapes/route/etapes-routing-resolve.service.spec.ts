jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IEtapes, Etapes } from '../etapes.model';
import { EtapesService } from '../service/etapes.service';

import { EtapesRoutingResolveService } from './etapes-routing-resolve.service';

describe('Service Tests', () => {
  describe('Etapes routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: EtapesRoutingResolveService;
    let service: EtapesService;
    let resultEtapes: IEtapes | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(EtapesRoutingResolveService);
      service = TestBed.inject(EtapesService);
      resultEtapes = undefined;
    });

    describe('resolve', () => {
      it('should return IEtapes returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEtapes = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultEtapes).toEqual({ id: 123 });
      });

      it('should return new IEtapes if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEtapes = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultEtapes).toEqual(new Etapes());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEtapes = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultEtapes).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
