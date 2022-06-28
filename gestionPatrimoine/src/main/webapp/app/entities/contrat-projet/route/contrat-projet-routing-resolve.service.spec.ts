jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IContratProjet, ContratProjet } from '../contrat-projet.model';
import { ContratProjetService } from '../service/contrat-projet.service';

import { ContratProjetRoutingResolveService } from './contrat-projet-routing-resolve.service';

describe('Service Tests', () => {
  describe('ContratProjet routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ContratProjetRoutingResolveService;
    let service: ContratProjetService;
    let resultContratProjet: IContratProjet | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ContratProjetRoutingResolveService);
      service = TestBed.inject(ContratProjetService);
      resultContratProjet = undefined;
    });

    describe('resolve', () => {
      it('should return IContratProjet returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContratProjet = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultContratProjet).toEqual({ id: 123 });
      });

      it('should return new IContratProjet if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContratProjet = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultContratProjet).toEqual(new ContratProjet());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContratProjet = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultContratProjet).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
