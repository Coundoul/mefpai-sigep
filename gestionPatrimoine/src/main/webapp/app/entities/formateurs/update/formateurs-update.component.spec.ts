jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FormateursService } from '../service/formateurs.service';
import { IFormateurs, Formateurs } from '../formateurs.model';
import { IEtablissement } from 'app/entities/etablissement/etablissement.model';
import { EtablissementService } from 'app/entities/etablissement/service/etablissement.service';

import { FormateursUpdateComponent } from './formateurs-update.component';

describe('Component Tests', () => {
  describe('Formateurs Management Update Component', () => {
    let comp: FormateursUpdateComponent;
    let fixture: ComponentFixture<FormateursUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let formateursService: FormateursService;
    let etablissementService: EtablissementService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FormateursUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(FormateursUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FormateursUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      formateursService = TestBed.inject(FormateursService);
      etablissementService = TestBed.inject(EtablissementService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Etablissement query and add missing value', () => {
        const formateurs: IFormateurs = { id: 456 };
        const nomEtablissement: IEtablissement = { id: 14716 };
        formateurs.nomEtablissement = nomEtablissement;

        const etablissementCollection: IEtablissement[] = [{ id: 72045 }];
        spyOn(etablissementService, 'query').and.returnValue(of(new HttpResponse({ body: etablissementCollection })));
        const additionalEtablissements = [nomEtablissement];
        const expectedCollection: IEtablissement[] = [...additionalEtablissements, ...etablissementCollection];
        spyOn(etablissementService, 'addEtablissementToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ formateurs });
        comp.ngOnInit();

        expect(etablissementService.query).toHaveBeenCalled();
        expect(etablissementService.addEtablissementToCollectionIfMissing).toHaveBeenCalledWith(
          etablissementCollection,
          ...additionalEtablissements
        );
        expect(comp.etablissementsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const formateurs: IFormateurs = { id: 456 };
        const nomEtablissement: IEtablissement = { id: 78037 };
        formateurs.nomEtablissement = nomEtablissement;

        activatedRoute.data = of({ formateurs });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(formateurs));
        expect(comp.etablissementsSharedCollection).toContain(nomEtablissement);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const formateurs = { id: 123 };
        spyOn(formateursService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ formateurs });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: formateurs }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(formateursService.update).toHaveBeenCalledWith(formateurs);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const formateurs = new Formateurs();
        spyOn(formateursService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ formateurs });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: formateurs }));
        saveSubject.complete();

        // THEN
        expect(formateursService.create).toHaveBeenCalledWith(formateurs);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const formateurs = { id: 123 };
        spyOn(formateursService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ formateurs });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(formateursService.update).toHaveBeenCalledWith(formateurs);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackEtablissementById', () => {
        it('Should return tracked Etablissement primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackEtablissementById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
