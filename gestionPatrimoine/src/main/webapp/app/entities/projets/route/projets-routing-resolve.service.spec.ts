jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IProjets, Projets } from '../projets.model';
import { ProjetsService } from '../service/projets.service';

import { ProjetsRoutingResolveService } from './projets-routing-resolve.service';

describe('Service Tests', () => {
  describe('Projets routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ProjetsRoutingResolveService;
    let service: ProjetsService;
    let resultProjets: IProjets | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ProjetsRoutingResolveService);
      service = TestBed.inject(ProjetsService);
      resultProjets = undefined;
    });

    describe('resolve', () => {
      it('should return IProjets returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultProjets = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultProjets).toEqual({ id: 123 });
      });

      it('should return new IProjets if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultProjets = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultProjets).toEqual(new Projets());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultProjets = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultProjets).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
