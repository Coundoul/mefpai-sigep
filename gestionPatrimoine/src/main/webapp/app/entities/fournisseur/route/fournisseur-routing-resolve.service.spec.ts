jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IFournisseur, Fournisseur } from '../fournisseur.model';
import { FournisseurService } from '../service/fournisseur.service';

import { FournisseurRoutingResolveService } from './fournisseur-routing-resolve.service';

describe('Service Tests', () => {
  describe('Fournisseur routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: FournisseurRoutingResolveService;
    let service: FournisseurService;
    let resultFournisseur: IFournisseur | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(FournisseurRoutingResolveService);
      service = TestBed.inject(FournisseurService);
      resultFournisseur = undefined;
    });

    describe('resolve', () => {
      it('should return IFournisseur returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFournisseur = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultFournisseur).toEqual({ id: 123 });
      });

      it('should return new IFournisseur if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFournisseur = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultFournisseur).toEqual(new Fournisseur());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFournisseur = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultFournisseur).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
