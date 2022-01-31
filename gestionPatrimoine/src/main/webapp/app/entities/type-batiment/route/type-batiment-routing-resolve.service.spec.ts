jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITypeBatiment, TypeBatiment } from '../type-batiment.model';
import { TypeBatimentService } from '../service/type-batiment.service';

import { TypeBatimentRoutingResolveService } from './type-batiment-routing-resolve.service';

describe('Service Tests', () => {
  describe('TypeBatiment routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: TypeBatimentRoutingResolveService;
    let service: TypeBatimentService;
    let resultTypeBatiment: ITypeBatiment | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(TypeBatimentRoutingResolveService);
      service = TestBed.inject(TypeBatimentService);
      resultTypeBatiment = undefined;
    });

    describe('resolve', () => {
      it('should return ITypeBatiment returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTypeBatiment = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTypeBatiment).toEqual({ id: 123 });
      });

      it('should return new ITypeBatiment if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTypeBatiment = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultTypeBatiment).toEqual(new TypeBatiment());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTypeBatiment = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTypeBatiment).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
