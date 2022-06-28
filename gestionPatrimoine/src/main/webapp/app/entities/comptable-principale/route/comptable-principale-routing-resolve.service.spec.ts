jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IComptablePrincipale, ComptablePrincipale } from '../comptable-principale.model';
import { ComptablePrincipaleService } from '../service/comptable-principale.service';

import { ComptablePrincipaleRoutingResolveService } from './comptable-principale-routing-resolve.service';

describe('Service Tests', () => {
  describe('ComptablePrincipale routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ComptablePrincipaleRoutingResolveService;
    let service: ComptablePrincipaleService;
    let resultComptablePrincipale: IComptablePrincipale | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ComptablePrincipaleRoutingResolveService);
      service = TestBed.inject(ComptablePrincipaleService);
      resultComptablePrincipale = undefined;
    });

    describe('resolve', () => {
      it('should return IComptablePrincipale returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultComptablePrincipale = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultComptablePrincipale).toEqual({ id: 123 });
      });

      it('should return new IComptablePrincipale if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultComptablePrincipale = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultComptablePrincipale).toEqual(new ComptablePrincipale());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultComptablePrincipale = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultComptablePrincipale).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
