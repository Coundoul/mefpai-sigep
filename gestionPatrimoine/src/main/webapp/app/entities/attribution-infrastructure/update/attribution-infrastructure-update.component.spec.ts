jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AttributionInfrastructureService } from '../service/attribution-infrastructure.service';
import { IAttributionInfrastructure, AttributionInfrastructure } from '../attribution-infrastructure.model';
import { IEtablissement } from 'app/entities/etablissement/etablissement.model';
import { EtablissementService } from 'app/entities/etablissement/service/etablissement.service';

import { AttributionInfrastructureUpdateComponent } from './attribution-infrastructure-update.component';

describe('Component Tests', () => {
  describe('AttributionInfrastructure Management Update Component', () => {
    let comp: AttributionInfrastructureUpdateComponent;
    let fixture: ComponentFixture<AttributionInfrastructureUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let attributionInfrastructureService: AttributionInfrastructureService;
    let etablissementService: EtablissementService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AttributionInfrastructureUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AttributionInfrastructureUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AttributionInfrastructureUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      attributionInfrastructureService = TestBed.inject(AttributionInfrastructureService);
      etablissementService = TestBed.inject(EtablissementService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Etablissement query and add missing value', () => {
        const attributionInfrastructure: IAttributionInfrastructure = { id: 456 };
        const nomEtablissement: IEtablissement = { id: 62204 };
        attributionInfrastructure.nomEtablissement = nomEtablissement;

        const etablissementCollection: IEtablissement[] = [{ id: 23913 }];
        spyOn(etablissementService, 'query').and.returnValue(of(new HttpResponse({ body: etablissementCollection })));
        const additionalEtablissements = [nomEtablissement];
        const expectedCollection: IEtablissement[] = [...additionalEtablissements, ...etablissementCollection];
        spyOn(etablissementService, 'addEtablissementToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ attributionInfrastructure });
        comp.ngOnInit();

        expect(etablissementService.query).toHaveBeenCalled();
        expect(etablissementService.addEtablissementToCollectionIfMissing).toHaveBeenCalledWith(
          etablissementCollection,
          ...additionalEtablissements
        );
        expect(comp.etablissementsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const attributionInfrastructure: IAttributionInfrastructure = { id: 456 };
        const nomEtablissement: IEtablissement = { id: 81011 };
        attributionInfrastructure.nomEtablissement = nomEtablissement;

        activatedRoute.data = of({ attributionInfrastructure });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(attributionInfrastructure));
        expect(comp.etablissementsSharedCollection).toContain(nomEtablissement);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const attributionInfrastructure = { id: 123 };
        spyOn(attributionInfrastructureService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ attributionInfrastructure });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: attributionInfrastructure }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(attributionInfrastructureService.update).toHaveBeenCalledWith(attributionInfrastructure);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const attributionInfrastructure = new AttributionInfrastructure();
        spyOn(attributionInfrastructureService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ attributionInfrastructure });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: attributionInfrastructure }));
        saveSubject.complete();

        // THEN
        expect(attributionInfrastructureService.create).toHaveBeenCalledWith(attributionInfrastructure);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const attributionInfrastructure = { id: 123 };
        spyOn(attributionInfrastructureService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ attributionInfrastructure });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(attributionInfrastructureService.update).toHaveBeenCalledWith(attributionInfrastructure);
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
