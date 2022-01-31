jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IMangasinierFichiste, MangasinierFichiste } from '../mangasinier-fichiste.model';
import { MangasinierFichisteService } from '../service/mangasinier-fichiste.service';

import { MangasinierFichisteRoutingResolveService } from './mangasinier-fichiste-routing-resolve.service';

describe('Service Tests', () => {
  describe('MangasinierFichiste routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: MangasinierFichisteRoutingResolveService;
    let service: MangasinierFichisteService;
    let resultMangasinierFichiste: IMangasinierFichiste | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(MangasinierFichisteRoutingResolveService);
      service = TestBed.inject(MangasinierFichisteService);
      resultMangasinierFichiste = undefined;
    });

    describe('resolve', () => {
      it('should return IMangasinierFichiste returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMangasinierFichiste = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMangasinierFichiste).toEqual({ id: 123 });
      });

      it('should return new IMangasinierFichiste if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMangasinierFichiste = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultMangasinierFichiste).toEqual(new MangasinierFichiste());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMangasinierFichiste = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMangasinierFichiste).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
