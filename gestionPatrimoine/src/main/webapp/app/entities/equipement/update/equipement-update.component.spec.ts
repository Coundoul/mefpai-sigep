jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { EquipementService } from '../service/equipement.service';
import { IEquipement, Equipement } from '../equipement.model';
import { IMagazin } from 'app/entities/magazin/magazin.model';
import { MagazinService } from 'app/entities/magazin/service/magazin.service';
import { IFournisseur } from 'app/entities/fournisseur/fournisseur.model';
import { FournisseurService } from 'app/entities/fournisseur/service/fournisseur.service';
import { IBon } from 'app/entities/bon/bon.model';
import { BonService } from 'app/entities/bon/service/bon.service';
import { ICategorieMatiere } from 'app/entities/categorie-matiere/categorie-matiere.model';
import { CategorieMatiereService } from 'app/entities/categorie-matiere/service/categorie-matiere.service';

import { EquipementUpdateComponent } from './equipement-update.component';

describe('Component Tests', () => {
  describe('Equipement Management Update Component', () => {
    let comp: EquipementUpdateComponent;
    let fixture: ComponentFixture<EquipementUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let equipementService: EquipementService;
    let magazinService: MagazinService;
    let fournisseurService: FournisseurService;
    let bonService: BonService;
    let categorieMatiereService: CategorieMatiereService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EquipementUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(EquipementUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EquipementUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      equipementService = TestBed.inject(EquipementService);
      magazinService = TestBed.inject(MagazinService);
      fournisseurService = TestBed.inject(FournisseurService);
      bonService = TestBed.inject(BonService);
      categorieMatiereService = TestBed.inject(CategorieMatiereService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Magazin query and add missing value', () => {
        const equipement: IEquipement = { id: 456 };
        const nomMagazin: IMagazin = { id: 61369 };
        equipement.nomMagazin = nomMagazin;

        const magazinCollection: IMagazin[] = [{ id: 79793 }];
        spyOn(magazinService, 'query').and.returnValue(of(new HttpResponse({ body: magazinCollection })));
        const additionalMagazins = [nomMagazin];
        const expectedCollection: IMagazin[] = [...additionalMagazins, ...magazinCollection];
        spyOn(magazinService, 'addMagazinToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ equipement });
        comp.ngOnInit();

        expect(magazinService.query).toHaveBeenCalled();
        expect(magazinService.addMagazinToCollectionIfMissing).toHaveBeenCalledWith(magazinCollection, ...additionalMagazins);
        expect(comp.magazinsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Fournisseur query and add missing value', () => {
        const equipement: IEquipement = { id: 456 };
        const nomFournisseur: IFournisseur = { id: 5726 };
        equipement.nomFournisseur = nomFournisseur;

        const fournisseurCollection: IFournisseur[] = [{ id: 97281 }];
        spyOn(fournisseurService, 'query').and.returnValue(of(new HttpResponse({ body: fournisseurCollection })));
        const additionalFournisseurs = [nomFournisseur];
        const expectedCollection: IFournisseur[] = [...additionalFournisseurs, ...fournisseurCollection];
        spyOn(fournisseurService, 'addFournisseurToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ equipement });
        comp.ngOnInit();

        expect(fournisseurService.query).toHaveBeenCalled();
        expect(fournisseurService.addFournisseurToCollectionIfMissing).toHaveBeenCalledWith(
          fournisseurCollection,
          ...additionalFournisseurs
        );
        expect(comp.fournisseursSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Bon query and add missing value', () => {
        const equipement: IEquipement = { id: 456 };
        const bon: IBon = { id: 62330 };
        equipement.bon = bon;

        const bonCollection: IBon[] = [{ id: 85391 }];
        spyOn(bonService, 'query').and.returnValue(of(new HttpResponse({ body: bonCollection })));
        const additionalBons = [bon];
        const expectedCollection: IBon[] = [...additionalBons, ...bonCollection];
        spyOn(bonService, 'addBonToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ equipement });
        comp.ngOnInit();

        expect(bonService.query).toHaveBeenCalled();
        expect(bonService.addBonToCollectionIfMissing).toHaveBeenCalledWith(bonCollection, ...additionalBons);
        expect(comp.bonsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call CategorieMatiere query and add missing value', () => {
        const equipement: IEquipement = { id: 456 };
        const categorie: ICategorieMatiere = { id: 92889 };
        equipement.categorie = categorie;

        const categorieMatiereCollection: ICategorieMatiere[] = [{ id: 7172 }];
        spyOn(categorieMatiereService, 'query').and.returnValue(of(new HttpResponse({ body: categorieMatiereCollection })));
        const additionalCategorieMatieres = [categorie];
        const expectedCollection: ICategorieMatiere[] = [...additionalCategorieMatieres, ...categorieMatiereCollection];
        spyOn(categorieMatiereService, 'addCategorieMatiereToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ equipement });
        comp.ngOnInit();

        expect(categorieMatiereService.query).toHaveBeenCalled();
        expect(categorieMatiereService.addCategorieMatiereToCollectionIfMissing).toHaveBeenCalledWith(
          categorieMatiereCollection,
          ...additionalCategorieMatieres
        );
        expect(comp.categorieMatieresSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const equipement: IEquipement = { id: 456 };
        const nomMagazin: IMagazin = { id: 99716 };
        equipement.nomMagazin = nomMagazin;
        const nomFournisseur: IFournisseur = { id: 31214 };
        equipement.nomFournisseur = nomFournisseur;
        const bon: IBon = { id: 16529 };
        equipement.bon = bon;
        const categorie: ICategorieMatiere = { id: 51453 };
        equipement.categorie = categorie;

        activatedRoute.data = of({ equipement });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(equipement));
        expect(comp.magazinsSharedCollection).toContain(nomMagazin);
        expect(comp.fournisseursSharedCollection).toContain(nomFournisseur);
        expect(comp.bonsSharedCollection).toContain(bon);
        expect(comp.categorieMatieresSharedCollection).toContain(categorie);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const equipement = { id: 123 };
        spyOn(equipementService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ equipement });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: equipement }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(equipementService.update).toHaveBeenCalledWith(equipement);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const equipement = new Equipement();
        spyOn(equipementService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ equipement });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: equipement }));
        saveSubject.complete();

        // THEN
        expect(equipementService.create).toHaveBeenCalledWith(equipement);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const equipement = { id: 123 };
        spyOn(equipementService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ equipement });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(equipementService.update).toHaveBeenCalledWith(equipement);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackMagazinById', () => {
        it('Should return tracked Magazin primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackMagazinById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackFournisseurById', () => {
        it('Should return tracked Fournisseur primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackFournisseurById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackBonById', () => {
        it('Should return tracked Bon primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackBonById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackCategorieMatiereById', () => {
        it('Should return tracked CategorieMatiere primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCategorieMatiereById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
