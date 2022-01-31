jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FicheTechniqueService } from '../service/fiche-technique.service';
import { IFicheTechnique, FicheTechnique } from '../fiche-technique.model';
import { IResponsable } from 'app/entities/responsable/responsable.model';
import { ResponsableService } from 'app/entities/responsable/service/responsable.service';

import { FicheTechniqueUpdateComponent } from './fiche-technique-update.component';

describe('Component Tests', () => {
  describe('FicheTechnique Management Update Component', () => {
    let comp: FicheTechniqueUpdateComponent;
    let fixture: ComponentFixture<FicheTechniqueUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let ficheTechniqueService: FicheTechniqueService;
    let responsableService: ResponsableService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FicheTechniqueUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(FicheTechniqueUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FicheTechniqueUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      ficheTechniqueService = TestBed.inject(FicheTechniqueService);
      responsableService = TestBed.inject(ResponsableService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Responsable query and add missing value', () => {
        const ficheTechnique: IFicheTechnique = { id: 456 };
        const nomResponsable: IResponsable = { id: 78918 };
        ficheTechnique.nomResponsable = nomResponsable;

        const responsableCollection: IResponsable[] = [{ id: 1743 }];
        spyOn(responsableService, 'query').and.returnValue(of(new HttpResponse({ body: responsableCollection })));
        const additionalResponsables = [nomResponsable];
        const expectedCollection: IResponsable[] = [...additionalResponsables, ...responsableCollection];
        spyOn(responsableService, 'addResponsableToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ ficheTechnique });
        comp.ngOnInit();

        expect(responsableService.query).toHaveBeenCalled();
        expect(responsableService.addResponsableToCollectionIfMissing).toHaveBeenCalledWith(
          responsableCollection,
          ...additionalResponsables
        );
        expect(comp.responsablesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const ficheTechnique: IFicheTechnique = { id: 456 };
        const nomResponsable: IResponsable = { id: 94629 };
        ficheTechnique.nomResponsable = nomResponsable;

        activatedRoute.data = of({ ficheTechnique });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(ficheTechnique));
        expect(comp.responsablesSharedCollection).toContain(nomResponsable);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const ficheTechnique = { id: 123 };
        spyOn(ficheTechniqueService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ ficheTechnique });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: ficheTechnique }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(ficheTechniqueService.update).toHaveBeenCalledWith(ficheTechnique);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const ficheTechnique = new FicheTechnique();
        spyOn(ficheTechniqueService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ ficheTechnique });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: ficheTechnique }));
        saveSubject.complete();

        // THEN
        expect(ficheTechniqueService.create).toHaveBeenCalledWith(ficheTechnique);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const ficheTechnique = { id: 123 };
        spyOn(ficheTechniqueService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ ficheTechnique });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(ficheTechniqueService.update).toHaveBeenCalledWith(ficheTechnique);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackResponsableById', () => {
        it('Should return tracked Responsable primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackResponsableById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
