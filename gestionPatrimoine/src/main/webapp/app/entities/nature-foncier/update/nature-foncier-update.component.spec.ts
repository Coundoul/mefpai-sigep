jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { NatureFoncierService } from '../service/nature-foncier.service';
import { INatureFoncier, NatureFoncier } from '../nature-foncier.model';
import { ICorpsEtat } from 'app/entities/corps-etat/corps-etat.model';
import { CorpsEtatService } from 'app/entities/corps-etat/service/corps-etat.service';

import { NatureFoncierUpdateComponent } from './nature-foncier-update.component';

describe('Component Tests', () => {
  describe('NatureFoncier Management Update Component', () => {
    let comp: NatureFoncierUpdateComponent;
    let fixture: ComponentFixture<NatureFoncierUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let natureFoncierService: NatureFoncierService;
    let corpsEtatService: CorpsEtatService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [NatureFoncierUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(NatureFoncierUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(NatureFoncierUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      natureFoncierService = TestBed.inject(NatureFoncierService);
      corpsEtatService = TestBed.inject(CorpsEtatService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call CorpsEtat query and add missing value', () => {
        const natureFoncier: INatureFoncier = { id: 456 };
        const nomCorps: ICorpsEtat = { id: 78370 };
        natureFoncier.nomCorps = nomCorps;

        const corpsEtatCollection: ICorpsEtat[] = [{ id: 18182 }];
        spyOn(corpsEtatService, 'query').and.returnValue(of(new HttpResponse({ body: corpsEtatCollection })));
        const additionalCorpsEtats = [nomCorps];
        const expectedCollection: ICorpsEtat[] = [...additionalCorpsEtats, ...corpsEtatCollection];
        spyOn(corpsEtatService, 'addCorpsEtatToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ natureFoncier });
        comp.ngOnInit();

        expect(corpsEtatService.query).toHaveBeenCalled();
        expect(corpsEtatService.addCorpsEtatToCollectionIfMissing).toHaveBeenCalledWith(corpsEtatCollection, ...additionalCorpsEtats);
        expect(comp.corpsEtatsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const natureFoncier: INatureFoncier = { id: 456 };
        const nomCorps: ICorpsEtat = { id: 68002 };
        natureFoncier.nomCorps = nomCorps;

        activatedRoute.data = of({ natureFoncier });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(natureFoncier));
        expect(comp.corpsEtatsSharedCollection).toContain(nomCorps);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const natureFoncier = { id: 123 };
        spyOn(natureFoncierService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ natureFoncier });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: natureFoncier }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(natureFoncierService.update).toHaveBeenCalledWith(natureFoncier);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const natureFoncier = new NatureFoncier();
        spyOn(natureFoncierService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ natureFoncier });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: natureFoncier }));
        saveSubject.complete();

        // THEN
        expect(natureFoncierService.create).toHaveBeenCalledWith(natureFoncier);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const natureFoncier = { id: 123 };
        spyOn(natureFoncierService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ natureFoncier });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(natureFoncierService.update).toHaveBeenCalledWith(natureFoncier);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCorpsEtatById', () => {
        it('Should return tracked CorpsEtat primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCorpsEtatById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
