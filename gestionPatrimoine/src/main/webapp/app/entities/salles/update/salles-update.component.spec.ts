jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SallesService } from '../service/salles.service';
import { ISalles, Salles } from '../salles.model';
import { IBatiment } from 'app/entities/batiment/batiment.model';
import { BatimentService } from 'app/entities/batiment/service/batiment.service';

import { SallesUpdateComponent } from './salles-update.component';

describe('Component Tests', () => {
  describe('Salles Management Update Component', () => {
    let comp: SallesUpdateComponent;
    let fixture: ComponentFixture<SallesUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let sallesService: SallesService;
    let batimentService: BatimentService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SallesUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(SallesUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SallesUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      sallesService = TestBed.inject(SallesService);
      batimentService = TestBed.inject(BatimentService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Batiment query and add missing value', () => {
        const salles: ISalles = { id: 456 };
        const nomBatiment: IBatiment = { id: 12036 };
        salles.nomBatiment = nomBatiment;

        const batimentCollection: IBatiment[] = [{ id: 28342 }];
        spyOn(batimentService, 'query').and.returnValue(of(new HttpResponse({ body: batimentCollection })));
        const additionalBatiments = [nomBatiment];
        const expectedCollection: IBatiment[] = [...additionalBatiments, ...batimentCollection];
        spyOn(batimentService, 'addBatimentToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ salles });
        comp.ngOnInit();

        expect(batimentService.query).toHaveBeenCalled();
        expect(batimentService.addBatimentToCollectionIfMissing).toHaveBeenCalledWith(batimentCollection, ...additionalBatiments);
        expect(comp.batimentsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const salles: ISalles = { id: 456 };
        const nomBatiment: IBatiment = { id: 97338 };
        salles.nomBatiment = nomBatiment;

        activatedRoute.data = of({ salles });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(salles));
        expect(comp.batimentsSharedCollection).toContain(nomBatiment);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const salles = { id: 123 };
        spyOn(sallesService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ salles });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: salles }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(sallesService.update).toHaveBeenCalledWith(salles);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const salles = new Salles();
        spyOn(sallesService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ salles });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: salles }));
        saveSubject.complete();

        // THEN
        expect(sallesService.create).toHaveBeenCalledWith(salles);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const salles = { id: 123 };
        spyOn(sallesService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ salles });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(sallesService.update).toHaveBeenCalledWith(salles);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackBatimentById', () => {
        it('Should return tracked Batiment primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackBatimentById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
