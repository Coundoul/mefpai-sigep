jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IFicheTechnique, FicheTechnique } from '../fiche-technique.model';
import { FicheTechniqueService } from '../service/fiche-technique.service';

import { FicheTechniqueRoutingResolveService } from './fiche-technique-routing-resolve.service';

describe('Service Tests', () => {
  describe('FicheTechnique routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: FicheTechniqueRoutingResolveService;
    let service: FicheTechniqueService;
    let resultFicheTechnique: IFicheTechnique | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(FicheTechniqueRoutingResolveService);
      service = TestBed.inject(FicheTechniqueService);
      resultFicheTechnique = undefined;
    });

    describe('resolve', () => {
      it('should return IFicheTechnique returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFicheTechnique = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultFicheTechnique).toEqual({ id: 123 });
      });

      it('should return new IFicheTechnique if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFicheTechnique = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultFicheTechnique).toEqual(new FicheTechnique());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFicheTechnique = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultFicheTechnique).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
