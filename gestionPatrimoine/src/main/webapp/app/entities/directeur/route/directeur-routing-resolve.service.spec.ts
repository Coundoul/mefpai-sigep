jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDirecteur, Directeur } from '../directeur.model';
import { DirecteurService } from '../service/directeur.service';

import { DirecteurRoutingResolveService } from './directeur-routing-resolve.service';

describe('Service Tests', () => {
  describe('Directeur routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: DirecteurRoutingResolveService;
    let service: DirecteurService;
    let resultDirecteur: IDirecteur | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(DirecteurRoutingResolveService);
      service = TestBed.inject(DirecteurService);
      resultDirecteur = undefined;
    });

    describe('resolve', () => {
      it('should return IDirecteur returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDirecteur = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDirecteur).toEqual({ id: 123 });
      });

      it('should return new IDirecteur if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDirecteur = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultDirecteur).toEqual(new Directeur());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDirecteur = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDirecteur).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
