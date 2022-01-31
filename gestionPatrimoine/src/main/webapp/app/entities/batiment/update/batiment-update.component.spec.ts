jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { BatimentService } from '../service/batiment.service';
import { IBatiment, Batiment } from '../batiment.model';
import { IEtablissement } from 'app/entities/etablissement/etablissement.model';
import { EtablissementService } from 'app/entities/etablissement/service/etablissement.service';
import { ICorpsEtat } from 'app/entities/corps-etat/corps-etat.model';
import { CorpsEtatService } from 'app/entities/corps-etat/service/corps-etat.service';

import { BatimentUpdateComponent } from './batiment-update.component';

describe('Component Tests', () => {
  describe('Batiment Management Update Component', () => {
    let comp: BatimentUpdateComponent;
    let fixture: ComponentFixture<BatimentUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let batimentService: BatimentService;
    let etablissementService: EtablissementService;
    let corpsEtatService: CorpsEtatService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [BatimentUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(BatimentUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BatimentUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      batimentService = TestBed.inject(BatimentService);
      etablissementService = TestBed.inject(EtablissementService);
      corpsEtatService = TestBed.inject(CorpsEtatService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Etablissement query and add missing value', () => {
        const batiment: IBatiment = { id: 456 };
        const nomEtablissement: IEtablissement = { id: 34621 };
        batiment.nomEtablissement = nomEtablissement;

        const etablissementCollection: IEtablissement[] = [{ id: 50896 }];
        spyOn(etablissementService, 'query').and.returnValue(of(new HttpResponse({ body: etablissementCollection })));
        const additionalEtablissements = [nomEtablissement];
        const expectedCollection: IEtablissement[] = [...additionalEtablissements, ...etablissementCollection];
        spyOn(etablissementService, 'addEtablissementToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ batiment });
        comp.ngOnInit();

        expect(etablissementService.query).toHaveBeenCalled();
        expect(etablissementService.addEtablissementToCollectionIfMissing).toHaveBeenCalledWith(
          etablissementCollection,
          ...additionalEtablissements
        );
        expect(comp.etablissementsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call CorpsEtat query and add missing value', () => {
        const batiment: IBatiment = { id: 456 };
        const nomCorps: ICorpsEtat = { id: 97121 };
        batiment.nomCorps = nomCorps;

        const corpsEtatCollection: ICorpsEtat[] = [{ id: 67042 }];
        spyOn(corpsEtatService, 'query').and.returnValue(of(new HttpResponse({ body: corpsEtatCollection })));
        const additionalCorpsEtats = [nomCorps];
        const expectedCollection: ICorpsEtat[] = [...additionalCorpsEtats, ...corpsEtatCollection];
        spyOn(corpsEtatService, 'addCorpsEtatToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ batiment });
        comp.ngOnInit();

        expect(corpsEtatService.query).toHaveBeenCalled();
        expect(corpsEtatService.addCorpsEtatToCollectionIfMissing).toHaveBeenCalledWith(corpsEtatCollection, ...additionalCorpsEtats);
        expect(comp.corpsEtatsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const batiment: IBatiment = { id: 456 };
        const nomEtablissement: IEtablissement = { id: 16210 };
        batiment.nomEtablissement = nomEtablissement;
        const nomCorps: ICorpsEtat = { id: 65309 };
        batiment.nomCorps = nomCorps;

        activatedRoute.data = of({ batiment });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(batiment));
        expect(comp.etablissementsSharedCollection).toContain(nomEtablissement);
        expect(comp.corpsEtatsSharedCollection).toContain(nomCorps);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const batiment = { id: 123 };
        spyOn(batimentService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ batiment });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: batiment }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(batimentService.update).toHaveBeenCalledWith(batiment);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const batiment = new Batiment();
        spyOn(batimentService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ batiment });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: batiment }));
        saveSubject.complete();

        // THEN
        expect(batimentService.create).toHaveBeenCalledWith(batiment);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const batiment = { id: 123 };
        spyOn(batimentService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ batiment });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(batimentService.update).toHaveBeenCalledWith(batiment);
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
