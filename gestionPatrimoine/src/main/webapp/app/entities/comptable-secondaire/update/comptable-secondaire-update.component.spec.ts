jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ComptableSecondaireService } from '../service/comptable-secondaire.service';
import { IComptableSecondaire, ComptableSecondaire } from '../comptable-secondaire.model';
import { IComptablePrincipale } from 'app/entities/comptable-principale/comptable-principale.model';
import { ComptablePrincipaleService } from 'app/entities/comptable-principale/service/comptable-principale.service';

import { ComptableSecondaireUpdateComponent } from './comptable-secondaire-update.component';

describe('Component Tests', () => {
  describe('ComptableSecondaire Management Update Component', () => {
    let comp: ComptableSecondaireUpdateComponent;
    let fixture: ComponentFixture<ComptableSecondaireUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let comptableSecondaireService: ComptableSecondaireService;
    let comptablePrincipaleService: ComptablePrincipaleService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ComptableSecondaireUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ComptableSecondaireUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ComptableSecondaireUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      comptableSecondaireService = TestBed.inject(ComptableSecondaireService);
      comptablePrincipaleService = TestBed.inject(ComptablePrincipaleService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call ComptablePrincipale query and add missing value', () => {
        const comptableSecondaire: IComptableSecondaire = { id: 456 };
        const comptablePrincipale: IComptablePrincipale = { id: 50968 };
        comptableSecondaire.comptablePrincipale = comptablePrincipale;

        const comptablePrincipaleCollection: IComptablePrincipale[] = [{ id: 56578 }];
        spyOn(comptablePrincipaleService, 'query').and.returnValue(of(new HttpResponse({ body: comptablePrincipaleCollection })));
        const additionalComptablePrincipales = [comptablePrincipale];
        const expectedCollection: IComptablePrincipale[] = [...additionalComptablePrincipales, ...comptablePrincipaleCollection];
        spyOn(comptablePrincipaleService, 'addComptablePrincipaleToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ comptableSecondaire });
        comp.ngOnInit();

        expect(comptablePrincipaleService.query).toHaveBeenCalled();
        expect(comptablePrincipaleService.addComptablePrincipaleToCollectionIfMissing).toHaveBeenCalledWith(
          comptablePrincipaleCollection,
          ...additionalComptablePrincipales
        );
        expect(comp.comptablePrincipalesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const comptableSecondaire: IComptableSecondaire = { id: 456 };
        const comptablePrincipale: IComptablePrincipale = { id: 31953 };
        comptableSecondaire.comptablePrincipale = comptablePrincipale;

        activatedRoute.data = of({ comptableSecondaire });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(comptableSecondaire));
        expect(comp.comptablePrincipalesSharedCollection).toContain(comptablePrincipale);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const comptableSecondaire = { id: 123 };
        spyOn(comptableSecondaireService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ comptableSecondaire });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: comptableSecondaire }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(comptableSecondaireService.update).toHaveBeenCalledWith(comptableSecondaire);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const comptableSecondaire = new ComptableSecondaire();
        spyOn(comptableSecondaireService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ comptableSecondaire });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: comptableSecondaire }));
        saveSubject.complete();

        // THEN
        expect(comptableSecondaireService.create).toHaveBeenCalledWith(comptableSecondaire);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const comptableSecondaire = { id: 123 };
        spyOn(comptableSecondaireService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ comptableSecondaire });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(comptableSecondaireService.update).toHaveBeenCalledWith(comptableSecondaire);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackComptablePrincipaleById', () => {
        it('Should return tracked ComptablePrincipale primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackComptablePrincipaleById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
