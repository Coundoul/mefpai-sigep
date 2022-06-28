jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IOrdonnaceurMatiere, OrdonnaceurMatiere } from '../ordonnaceur-matiere.model';
import { OrdonnaceurMatiereService } from '../service/ordonnaceur-matiere.service';

import { OrdonnaceurMatiereRoutingResolveService } from './ordonnaceur-matiere-routing-resolve.service';

describe('Service Tests', () => {
  describe('OrdonnaceurMatiere routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: OrdonnaceurMatiereRoutingResolveService;
    let service: OrdonnaceurMatiereService;
    let resultOrdonnaceurMatiere: IOrdonnaceurMatiere | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(OrdonnaceurMatiereRoutingResolveService);
      service = TestBed.inject(OrdonnaceurMatiereService);
      resultOrdonnaceurMatiere = undefined;
    });

    describe('resolve', () => {
      it('should return IOrdonnaceurMatiere returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOrdonnaceurMatiere = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultOrdonnaceurMatiere).toEqual({ id: 123 });
      });

      it('should return new IOrdonnaceurMatiere if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOrdonnaceurMatiere = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultOrdonnaceurMatiere).toEqual(new OrdonnaceurMatiere());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOrdonnaceurMatiere = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultOrdonnaceurMatiere).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
