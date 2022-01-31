jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AtelierService } from '../service/atelier.service';
import { IAtelier, Atelier } from '../atelier.model';
import { IFiliereStabilise } from 'app/entities/filiere-stabilise/filiere-stabilise.model';
import { FiliereStabiliseService } from 'app/entities/filiere-stabilise/service/filiere-stabilise.service';
import { IBatiment } from 'app/entities/batiment/batiment.model';
import { BatimentService } from 'app/entities/batiment/service/batiment.service';

import { AtelierUpdateComponent } from './atelier-update.component';

describe('Component Tests', () => {
  describe('Atelier Management Update Component', () => {
    let comp: AtelierUpdateComponent;
    let fixture: ComponentFixture<AtelierUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let atelierService: AtelierService;
    let filiereStabiliseService: FiliereStabiliseService;
    let batimentService: BatimentService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AtelierUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AtelierUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AtelierUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      atelierService = TestBed.inject(AtelierService);
      filiereStabiliseService = TestBed.inject(FiliereStabiliseService);
      batimentService = TestBed.inject(BatimentService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call FiliereStabilise query and add missing value', () => {
        const atelier: IAtelier = { id: 456 };
        const nomFiliere: IFiliereStabilise = { id: 26035 };
        atelier.nomFiliere = nomFiliere;

        const filiereStabiliseCollection: IFiliereStabilise[] = [{ id: 6263 }];
        spyOn(filiereStabiliseService, 'query').and.returnValue(of(new HttpResponse({ body: filiereStabiliseCollection })));
        const additionalFiliereStabilises = [nomFiliere];
        const expectedCollection: IFiliereStabilise[] = [...additionalFiliereStabilises, ...filiereStabiliseCollection];
        spyOn(filiereStabiliseService, 'addFiliereStabiliseToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ atelier });
        comp.ngOnInit();

        expect(filiereStabiliseService.query).toHaveBeenCalled();
        expect(filiereStabiliseService.addFiliereStabiliseToCollectionIfMissing).toHaveBeenCalledWith(
          filiereStabiliseCollection,
          ...additionalFiliereStabilises
        );
        expect(comp.filiereStabilisesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Batiment query and add missing value', () => {
        const atelier: IAtelier = { id: 456 };
        const nomBatiment: IBatiment = { id: 8497 };
        atelier.nomBatiment = nomBatiment;

        const batimentCollection: IBatiment[] = [{ id: 76419 }];
        spyOn(batimentService, 'query').and.returnValue(of(new HttpResponse({ body: batimentCollection })));
        const additionalBatiments = [nomBatiment];
        const expectedCollection: IBatiment[] = [...additionalBatiments, ...batimentCollection];
        spyOn(batimentService, 'addBatimentToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ atelier });
        comp.ngOnInit();

        expect(batimentService.query).toHaveBeenCalled();
        expect(batimentService.addBatimentToCollectionIfMissing).toHaveBeenCalledWith(batimentCollection, ...additionalBatiments);
        expect(comp.batimentsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const atelier: IAtelier = { id: 456 };
        const nomFiliere: IFiliereStabilise = { id: 54749 };
        atelier.nomFiliere = nomFiliere;
        const nomBatiment: IBatiment = { id: 78492 };
        atelier.nomBatiment = nomBatiment;

        activatedRoute.data = of({ atelier });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(atelier));
        expect(comp.filiereStabilisesSharedCollection).toContain(nomFiliere);
        expect(comp.batimentsSharedCollection).toContain(nomBatiment);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const atelier = { id: 123 };
        spyOn(atelierService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ atelier });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: atelier }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(atelierService.update).toHaveBeenCalledWith(atelier);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const atelier = new Atelier();
        spyOn(atelierService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ atelier });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: atelier }));
        saveSubject.complete();

        // THEN
        expect(atelierService.create).toHaveBeenCalledWith(atelier);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const atelier = { id: 123 };
        spyOn(atelierService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ atelier });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(atelierService.update).toHaveBeenCalledWith(atelier);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackFiliereStabiliseById', () => {
        it('Should return tracked FiliereStabilise primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackFiliereStabiliseById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackBatimentById', () => {
        it('Should return tracked Batiment primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackBatimentById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
