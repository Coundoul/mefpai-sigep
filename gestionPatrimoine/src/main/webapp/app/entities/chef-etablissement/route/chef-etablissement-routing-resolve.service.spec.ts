jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IChefEtablissement, ChefEtablissement } from '../chef-etablissement.model';
import { ChefEtablissementService } from '../service/chef-etablissement.service';

import { ChefEtablissementRoutingResolveService } from './chef-etablissement-routing-resolve.service';

describe('Service Tests', () => {
  describe('ChefEtablissement routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ChefEtablissementRoutingResolveService;
    let service: ChefEtablissementService;
    let resultChefEtablissement: IChefEtablissement | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ChefEtablissementRoutingResolveService);
      service = TestBed.inject(ChefEtablissementService);
      resultChefEtablissement = undefined;
    });

    describe('resolve', () => {
      it('should return IChefEtablissement returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultChefEtablissement = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultChefEtablissement).toEqual({ id: 123 });
      });

      it('should return new IChefEtablissement if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultChefEtablissement = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultChefEtablissement).toEqual(new ChefEtablissement());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultChefEtablissement = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultChefEtablissement).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
