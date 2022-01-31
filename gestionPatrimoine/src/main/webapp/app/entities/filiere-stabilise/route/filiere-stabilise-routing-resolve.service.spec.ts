jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IFiliereStabilise, FiliereStabilise } from '../filiere-stabilise.model';
import { FiliereStabiliseService } from '../service/filiere-stabilise.service';

import { FiliereStabiliseRoutingResolveService } from './filiere-stabilise-routing-resolve.service';

describe('Service Tests', () => {
  describe('FiliereStabilise routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: FiliereStabiliseRoutingResolveService;
    let service: FiliereStabiliseService;
    let resultFiliereStabilise: IFiliereStabilise | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(FiliereStabiliseRoutingResolveService);
      service = TestBed.inject(FiliereStabiliseService);
      resultFiliereStabilise = undefined;
    });

    describe('resolve', () => {
      it('should return IFiliereStabilise returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFiliereStabilise = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultFiliereStabilise).toEqual({ id: 123 });
      });

      it('should return new IFiliereStabilise if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFiliereStabilise = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultFiliereStabilise).toEqual(new FiliereStabilise());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFiliereStabilise = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultFiliereStabilise).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
