jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AttributionService } from '../service/attribution.service';
import { IAttribution, Attribution } from '../attribution.model';
import { IUtilisateurFinal } from 'app/entities/utilisateur-final/utilisateur-final.model';
import { UtilisateurFinalService } from 'app/entities/utilisateur-final/service/utilisateur-final.service';
import { IAffectations } from 'app/entities/affectations/affectations.model';
import { AffectationsService } from 'app/entities/affectations/service/affectations.service';

import { AttributionUpdateComponent } from './attribution-update.component';

describe('Component Tests', () => {
  describe('Attribution Management Update Component', () => {
    let comp: AttributionUpdateComponent;
    let fixture: ComponentFixture<AttributionUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let attributionService: AttributionService;
    let utilisateurFinalService: UtilisateurFinalService;
    let affectationsService: AffectationsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AttributionUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AttributionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AttributionUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      attributionService = TestBed.inject(AttributionService);
      utilisateurFinalService = TestBed.inject(UtilisateurFinalService);
      affectationsService = TestBed.inject(AffectationsService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call UtilisateurFinal query and add missing value', () => {
        const attribution: IAttribution = { id: 456 };
        const nomUtilisateur: IUtilisateurFinal = { id: 24567 };
        attribution.nomUtilisateur = nomUtilisateur;

        const utilisateurFinalCollection: IUtilisateurFinal[] = [{ id: 40362 }];
        spyOn(utilisateurFinalService, 'query').and.returnValue(of(new HttpResponse({ body: utilisateurFinalCollection })));
        const additionalUtilisateurFinals = [nomUtilisateur];
        const expectedCollection: IUtilisateurFinal[] = [...additionalUtilisateurFinals, ...utilisateurFinalCollection];
        spyOn(utilisateurFinalService, 'addUtilisateurFinalToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ attribution });
        comp.ngOnInit();

        expect(utilisateurFinalService.query).toHaveBeenCalled();
        expect(utilisateurFinalService.addUtilisateurFinalToCollectionIfMissing).toHaveBeenCalledWith(
          utilisateurFinalCollection,
          ...additionalUtilisateurFinals
        );
        expect(comp.utilisateurFinalsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Affectations query and add missing value', () => {
        const attribution: IAttribution = { id: 456 };
        const affectations: IAffectations = { id: 396 };
        attribution.affectations = affectations;

        const affectationsCollection: IAffectations[] = [{ id: 15201 }];
        spyOn(affectationsService, 'query').and.returnValue(of(new HttpResponse({ body: affectationsCollection })));
        const additionalAffectations = [affectations];
        const expectedCollection: IAffectations[] = [...additionalAffectations, ...affectationsCollection];
        spyOn(affectationsService, 'addAffectationsToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ attribution });
        comp.ngOnInit();

        expect(affectationsService.query).toHaveBeenCalled();
        expect(affectationsService.addAffectationsToCollectionIfMissing).toHaveBeenCalledWith(
          affectationsCollection,
          ...additionalAffectations
        );
        expect(comp.affectationsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const attribution: IAttribution = { id: 456 };
        const nomUtilisateur: IUtilisateurFinal = { id: 28482 };
        attribution.nomUtilisateur = nomUtilisateur;
        const affectations: IAffectations = { id: 62075 };
        attribution.affectations = affectations;

        activatedRoute.data = of({ attribution });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(attribution));
        expect(comp.utilisateurFinalsSharedCollection).toContain(nomUtilisateur);
        expect(comp.affectationsSharedCollection).toContain(affectations);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const attribution = { id: 123 };
        spyOn(attributionService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ attribution });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: attribution }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(attributionService.update).toHaveBeenCalledWith(attribution);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const attribution = new Attribution();
        spyOn(attributionService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ attribution });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: attribution }));
        saveSubject.complete();

        // THEN
        expect(attributionService.create).toHaveBeenCalledWith(attribution);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const attribution = { id: 123 };
        spyOn(attributionService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ attribution });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(attributionService.update).toHaveBeenCalledWith(attribution);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackUtilisateurFinalById', () => {
        it('Should return tracked UtilisateurFinal primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUtilisateurFinalById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackAffectationsById', () => {
        it('Should return tracked Affectations primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackAffectationsById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
