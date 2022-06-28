jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { EtablissementService } from '../service/etablissement.service';
import { IEtablissement, Etablissement } from '../etablissement.model';
import { IQuartier } from 'app/entities/quartier/quartier.model';
import { QuartierService } from 'app/entities/quartier/service/quartier.service';

import { EtablissementUpdateComponent } from './etablissement-update.component';

describe('Component Tests', () => {
  describe('Etablissement Management Update Component', () => {
    let comp: EtablissementUpdateComponent;
    let fixture: ComponentFixture<EtablissementUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let etablissementService: EtablissementService;
    let quartierService: QuartierService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EtablissementUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(EtablissementUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EtablissementUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      etablissementService = TestBed.inject(EtablissementService);
      quartierService = TestBed.inject(QuartierService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Quartier query and add missing value', () => {
        const etablissement: IEtablissement = { id: 456 };
        const nomQuartier: IQuartier = { id: 23940 };
        etablissement.nomQuartier = nomQuartier;

        const quartierCollection: IQuartier[] = [{ id: 55503 }];
        spyOn(quartierService, 'query').and.returnValue(of(new HttpResponse({ body: quartierCollection })));
        const additionalQuartiers = [nomQuartier];
        const expectedCollection: IQuartier[] = [...additionalQuartiers, ...quartierCollection];
        spyOn(quartierService, 'addQuartierToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ etablissement });
        comp.ngOnInit();

        expect(quartierService.query).toHaveBeenCalled();
        expect(quartierService.addQuartierToCollectionIfMissing).toHaveBeenCalledWith(quartierCollection, ...additionalQuartiers);
        expect(comp.quartiersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const etablissement: IEtablissement = { id: 456 };
        const nomQuartier: IQuartier = { id: 27150 };
        etablissement.nomQuartier = nomQuartier;

        activatedRoute.data = of({ etablissement });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(etablissement));
        expect(comp.quartiersSharedCollection).toContain(nomQuartier);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const etablissement = { id: 123 };
        spyOn(etablissementService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ etablissement });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: etablissement }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(etablissementService.update).toHaveBeenCalledWith(etablissement);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const etablissement = new Etablissement();
        spyOn(etablissementService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ etablissement });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: etablissement }));
        saveSubject.complete();

        // THEN
        expect(etablissementService.create).toHaveBeenCalledWith(etablissement);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const etablissement = { id: 123 };
        spyOn(etablissementService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ etablissement });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(etablissementService.update).toHaveBeenCalledWith(etablissement);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackQuartierById', () => {
        it('Should return tracked Quartier primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackQuartierById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
