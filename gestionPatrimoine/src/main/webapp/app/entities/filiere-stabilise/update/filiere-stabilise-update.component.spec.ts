jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FiliereStabiliseService } from '../service/filiere-stabilise.service';
import { IFiliereStabilise, FiliereStabilise } from '../filiere-stabilise.model';
import { IFormateurs } from 'app/entities/formateurs/formateurs.model';
import { FormateursService } from 'app/entities/formateurs/service/formateurs.service';

import { FiliereStabiliseUpdateComponent } from './filiere-stabilise-update.component';

describe('Component Tests', () => {
  describe('FiliereStabilise Management Update Component', () => {
    let comp: FiliereStabiliseUpdateComponent;
    let fixture: ComponentFixture<FiliereStabiliseUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let filiereStabiliseService: FiliereStabiliseService;
    let formateursService: FormateursService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FiliereStabiliseUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(FiliereStabiliseUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FiliereStabiliseUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      filiereStabiliseService = TestBed.inject(FiliereStabiliseService);
      formateursService = TestBed.inject(FormateursService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Formateurs query and add missing value', () => {
        const filiereStabilise: IFiliereStabilise = { id: 456 };
        const nomFormateur: IFormateurs = { id: 16973 };
        filiereStabilise.nomFormateur = nomFormateur;

        const formateursCollection: IFormateurs[] = [{ id: 14112 }];
        spyOn(formateursService, 'query').and.returnValue(of(new HttpResponse({ body: formateursCollection })));
        const additionalFormateurs = [nomFormateur];
        const expectedCollection: IFormateurs[] = [...additionalFormateurs, ...formateursCollection];
        spyOn(formateursService, 'addFormateursToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ filiereStabilise });
        comp.ngOnInit();

        expect(formateursService.query).toHaveBeenCalled();
        expect(formateursService.addFormateursToCollectionIfMissing).toHaveBeenCalledWith(formateursCollection, ...additionalFormateurs);
        expect(comp.formateursSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const filiereStabilise: IFiliereStabilise = { id: 456 };
        const nomFormateur: IFormateurs = { id: 18323 };
        filiereStabilise.nomFormateur = nomFormateur;

        activatedRoute.data = of({ filiereStabilise });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(filiereStabilise));
        expect(comp.formateursSharedCollection).toContain(nomFormateur);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const filiereStabilise = { id: 123 };
        spyOn(filiereStabiliseService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ filiereStabilise });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: filiereStabilise }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(filiereStabiliseService.update).toHaveBeenCalledWith(filiereStabilise);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const filiereStabilise = new FiliereStabilise();
        spyOn(filiereStabiliseService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ filiereStabilise });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: filiereStabilise }));
        saveSubject.complete();

        // THEN
        expect(filiereStabiliseService.create).toHaveBeenCalledWith(filiereStabilise);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const filiereStabilise = { id: 123 };
        spyOn(filiereStabiliseService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ filiereStabilise });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(filiereStabiliseService.update).toHaveBeenCalledWith(filiereStabilise);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackFormateursById', () => {
        it('Should return tracked Formateurs primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackFormateursById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
