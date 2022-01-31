jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITechnicien, Technicien } from '../technicien.model';
import { TechnicienService } from '../service/technicien.service';

import { TechnicienRoutingResolveService } from './technicien-routing-resolve.service';

describe('Service Tests', () => {
  describe('Technicien routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: TechnicienRoutingResolveService;
    let service: TechnicienService;
    let resultTechnicien: ITechnicien | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(TechnicienRoutingResolveService);
      service = TestBed.inject(TechnicienService);
      resultTechnicien = undefined;
    });

    describe('resolve', () => {
      it('should return ITechnicien returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTechnicien = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTechnicien).toEqual({ id: 123 });
      });

      it('should return new ITechnicien if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTechnicien = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultTechnicien).toEqual(new Technicien());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTechnicien = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTechnicien).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
