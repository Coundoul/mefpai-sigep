jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ProjetsService } from '../service/projets.service';
import { IProjets, Projets } from '../projets.model';
import { IContratProjet } from 'app/entities/contrat-projet/contrat-projet.model';
import { ContratProjetService } from 'app/entities/contrat-projet/service/contrat-projet.service';
import { IEtablissement } from 'app/entities/etablissement/etablissement.model';
import { EtablissementService } from 'app/entities/etablissement/service/etablissement.service';

import { ProjetsUpdateComponent } from './projets-update.component';

describe('Component Tests', () => {
  describe('Projets Management Update Component', () => {
    let comp: ProjetsUpdateComponent;
    let fixture: ComponentFixture<ProjetsUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let projetsService: ProjetsService;
    let contratProjetService: ContratProjetService;
    let etablissementService: EtablissementService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ProjetsUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ProjetsUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProjetsUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      projetsService = TestBed.inject(ProjetsService);
      contratProjetService = TestBed.inject(ContratProjetService);
      etablissementService = TestBed.inject(EtablissementService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call nom query and add missing value', () => {
        const projets: IProjets = { id: 456 };
        const nom: IContratProjet = { id: 59400 };
        projets.nom = nom;

        const nomCollection: IContratProjet[] = [{ id: 25270 }];
        spyOn(contratProjetService, 'query').and.returnValue(of(new HttpResponse({ body: nomCollection })));
        const expectedCollection: IContratProjet[] = [nom, ...nomCollection];
        spyOn(contratProjetService, 'addContratProjetToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ projets });
        comp.ngOnInit();

        expect(contratProjetService.query).toHaveBeenCalled();
        expect(contratProjetService.addContratProjetToCollectionIfMissing).toHaveBeenCalledWith(nomCollection, nom);
        expect(comp.nomsCollection).toEqual(expectedCollection);
      });

      it('Should call Etablissement query and add missing value', () => {
        const projets: IProjets = { id: 456 };
        const nomEtablissement: IEtablissement = { id: 78395 };
        projets.nomEtablissement = nomEtablissement;
        const nomBatiment: IEtablissement = { id: 31753 };
        projets.nomBatiment = nomBatiment;

        const etablissementCollection: IEtablissement[] = [{ id: 83153 }];
        spyOn(etablissementService, 'query').and.returnValue(of(new HttpResponse({ body: etablissementCollection })));
        const additionalEtablissements = [nomEtablissement, nomBatiment];
        const expectedCollection: IEtablissement[] = [...additionalEtablissements, ...etablissementCollection];
        spyOn(etablissementService, 'addEtablissementToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ projets });
        comp.ngOnInit();

        expect(etablissementService.query).toHaveBeenCalled();
        expect(etablissementService.addEtablissementToCollectionIfMissing).toHaveBeenCalledWith(
          etablissementCollection,
          ...additionalEtablissements
        );
        expect(comp.etablissementsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const projets: IProjets = { id: 456 };
        const nom: IContratProjet = { id: 86322 };
        projets.nom = nom;
        const nomEtablissement: IEtablissement = { id: 26081 };
        projets.nomEtablissement = nomEtablissement;
        const nomBatiment: IEtablissement = { id: 55999 };
        projets.nomBatiment = nomBatiment;

        activatedRoute.data = of({ projets });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(projets));
        expect(comp.nomsCollection).toContain(nom);
        expect(comp.etablissementsSharedCollection).toContain(nomEtablissement);
        expect(comp.etablissementsSharedCollection).toContain(nomBatiment);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const projets = { id: 123 };
        spyOn(projetsService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ projets });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: projets }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(projetsService.update).toHaveBeenCalledWith(projets);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const projets = new Projets();
        spyOn(projetsService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ projets });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: projets }));
        saveSubject.complete();

        // THEN
        expect(projetsService.create).toHaveBeenCalledWith(projets);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const projets = { id: 123 };
        spyOn(projetsService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ projets });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(projetsService.update).toHaveBeenCalledWith(projets);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackContratProjetById', () => {
        it('Should return tracked ContratProjet primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackContratProjetById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

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
