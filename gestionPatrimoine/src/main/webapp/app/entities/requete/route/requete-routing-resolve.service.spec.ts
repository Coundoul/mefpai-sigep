jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IRequete, Requete } from '../requete.model';
import { RequeteService } from '../service/requete.service';

import { RequeteRoutingResolveService } from './requete-routing-resolve.service';

describe('Service Tests', () => {
  describe('Requete routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: RequeteRoutingResolveService;
    let service: RequeteService;
    let resultRequete: IRequete | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(RequeteRoutingResolveService);
      service = TestBed.inject(RequeteService);
      resultRequete = undefined;
    });

    describe('resolve', () => {
      it('should return IRequete returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultRequete = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultRequete).toEqual({ id: 123 });
      });

      it('should return new IRequete if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultRequete = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultRequete).toEqual(new Requete());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultRequete = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultRequete).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
