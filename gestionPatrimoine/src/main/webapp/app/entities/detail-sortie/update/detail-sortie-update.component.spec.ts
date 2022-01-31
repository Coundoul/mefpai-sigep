jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DetailSortieService } from '../service/detail-sortie.service';
import { IDetailSortie, DetailSortie } from '../detail-sortie.model';
import { IBon } from 'app/entities/bon/bon.model';
import { BonService } from 'app/entities/bon/service/bon.service';

import { DetailSortieUpdateComponent } from './detail-sortie-update.component';

describe('Component Tests', () => {
  describe('DetailSortie Management Update Component', () => {
    let comp: DetailSortieUpdateComponent;
    let fixture: ComponentFixture<DetailSortieUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let detailSortieService: DetailSortieService;
    let bonService: BonService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DetailSortieUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DetailSortieUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DetailSortieUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      detailSortieService = TestBed.inject(DetailSortieService);
      bonService = TestBed.inject(BonService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call typeBon query and add missing value', () => {
        const detailSortie: IDetailSortie = { id: 456 };
        const typeBon: IBon = { id: 1928 };
        detailSortie.typeBon = typeBon;

        const typeBonCollection: IBon[] = [{ id: 97299 }];
        spyOn(bonService, 'query').and.returnValue(of(new HttpResponse({ body: typeBonCollection })));
        const expectedCollection: IBon[] = [typeBon, ...typeBonCollection];
        spyOn(bonService, 'addBonToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ detailSortie });
        comp.ngOnInit();

        expect(bonService.query).toHaveBeenCalled();
        expect(bonService.addBonToCollectionIfMissing).toHaveBeenCalledWith(typeBonCollection, typeBon);
        expect(comp.typeBonsCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const detailSortie: IDetailSortie = { id: 456 };
        const typeBon: IBon = { id: 49461 };
        detailSortie.typeBon = typeBon;

        activatedRoute.data = of({ detailSortie });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(detailSortie));
        expect(comp.typeBonsCollection).toContain(typeBon);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const detailSortie = { id: 123 };
        spyOn(detailSortieService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ detailSortie });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: detailSortie }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(detailSortieService.update).toHaveBeenCalledWith(detailSortie);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const detailSortie = new DetailSortie();
        spyOn(detailSortieService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ detailSortie });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: detailSortie }));
        saveSubject.complete();

        // THEN
        expect(detailSortieService.create).toHaveBeenCalledWith(detailSortie);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const detailSortie = { id: 123 };
        spyOn(detailSortieService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ detailSortie });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(detailSortieService.update).toHaveBeenCalledWith(detailSortie);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackBonById', () => {
        it('Should return tracked Bon primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackBonById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
