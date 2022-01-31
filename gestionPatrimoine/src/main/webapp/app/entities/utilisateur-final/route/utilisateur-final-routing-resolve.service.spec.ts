jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IUtilisateurFinal, UtilisateurFinal } from '../utilisateur-final.model';
import { UtilisateurFinalService } from '../service/utilisateur-final.service';

import { UtilisateurFinalRoutingResolveService } from './utilisateur-final-routing-resolve.service';

describe('Service Tests', () => {
  describe('UtilisateurFinal routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: UtilisateurFinalRoutingResolveService;
    let service: UtilisateurFinalService;
    let resultUtilisateurFinal: IUtilisateurFinal | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(UtilisateurFinalRoutingResolveService);
      service = TestBed.inject(UtilisateurFinalService);
      resultUtilisateurFinal = undefined;
    });

    describe('resolve', () => {
      it('should return IUtilisateurFinal returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultUtilisateurFinal = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultUtilisateurFinal).toEqual({ id: 123 });
      });

      it('should return new IUtilisateurFinal if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultUtilisateurFinal = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultUtilisateurFinal).toEqual(new UtilisateurFinal());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultUtilisateurFinal = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultUtilisateurFinal).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
