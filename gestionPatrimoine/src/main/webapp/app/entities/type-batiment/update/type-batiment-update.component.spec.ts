jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TypeBatimentService } from '../service/type-batiment.service';
import { ITypeBatiment, TypeBatiment } from '../type-batiment.model';
import { IBatiment } from 'app/entities/batiment/batiment.model';
import { BatimentService } from 'app/entities/batiment/service/batiment.service';

import { TypeBatimentUpdateComponent } from './type-batiment-update.component';

describe('Component Tests', () => {
  describe('TypeBatiment Management Update Component', () => {
    let comp: TypeBatimentUpdateComponent;
    let fixture: ComponentFixture<TypeBatimentUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let typeBatimentService: TypeBatimentService;
    let batimentService: BatimentService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TypeBatimentUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TypeBatimentUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TypeBatimentUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      typeBatimentService = TestBed.inject(TypeBatimentService);
      batimentService = TestBed.inject(BatimentService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Batiment query and add missing value', () => {
        const typeBatiment: ITypeBatiment = { id: 456 };
        const nomBatiment: IBatiment = { id: 66108 };
        typeBatiment.nomBatiment = nomBatiment;

        const batimentCollection: IBatiment[] = [{ id: 70007 }];
        spyOn(batimentService, 'query').and.returnValue(of(new HttpResponse({ body: batimentCollection })));
        const additionalBatiments = [nomBatiment];
        const expectedCollection: IBatiment[] = [...additionalBatiments, ...batimentCollection];
        spyOn(batimentService, 'addBatimentToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ typeBatiment });
        comp.ngOnInit();

        expect(batimentService.query).toHaveBeenCalled();
        expect(batimentService.addBatimentToCollectionIfMissing).toHaveBeenCalledWith(batimentCollection, ...additionalBatiments);
        expect(comp.batimentsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const typeBatiment: ITypeBatiment = { id: 456 };
        const nomBatiment: IBatiment = { id: 98749 };
        typeBatiment.nomBatiment = nomBatiment;

        activatedRoute.data = of({ typeBatiment });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(typeBatiment));
        expect(comp.batimentsSharedCollection).toContain(nomBatiment);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const typeBatiment = { id: 123 };
        spyOn(typeBatimentService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ typeBatiment });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: typeBatiment }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(typeBatimentService.update).toHaveBeenCalledWith(typeBatiment);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const typeBatiment = new TypeBatiment();
        spyOn(typeBatimentService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ typeBatiment });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: typeBatiment }));
        saveSubject.complete();

        // THEN
        expect(typeBatimentService.create).toHaveBeenCalledWith(typeBatiment);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const typeBatiment = { id: 123 };
        spyOn(typeBatimentService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ typeBatiment });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(typeBatimentService.update).toHaveBeenCalledWith(typeBatiment);
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
