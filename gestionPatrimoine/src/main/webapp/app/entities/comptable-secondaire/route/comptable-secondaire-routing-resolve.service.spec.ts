jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IComptableSecondaire, ComptableSecondaire } from '../comptable-secondaire.model';
import { ComptableSecondaireService } from '../service/comptable-secondaire.service';

import { ComptableSecondaireRoutingResolveService } from './comptable-secondaire-routing-resolve.service';

describe('Service Tests', () => {
  describe('ComptableSecondaire routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ComptableSecondaireRoutingResolveService;
    let service: ComptableSecondaireService;
    let resultComptableSecondaire: IComptableSecondaire | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ComptableSecondaireRoutingResolveService);
      service = TestBed.inject(ComptableSecondaireService);
      resultComptableSecondaire = undefined;
    });

    describe('resolve', () => {
      it('should return IComptableSecondaire returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultComptableSecondaire = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultComptableSecondaire).toEqual({ id: 123 });
      });

      it('should return new IComptableSecondaire if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultComptableSecondaire = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultComptableSecondaire).toEqual(new ComptableSecondaire());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultComptableSecondaire = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultComptableSecondaire).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
