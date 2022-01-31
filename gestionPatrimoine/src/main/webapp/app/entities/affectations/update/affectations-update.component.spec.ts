jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AffectationsService } from '../service/affectations.service';
import { IAffectations, Affectations } from '../affectations.model';
import { IEquipement } from 'app/entities/equipement/equipement.model';
import { EquipementService } from 'app/entities/equipement/service/equipement.service';

import { AffectationsUpdateComponent } from './affectations-update.component';

describe('Component Tests', () => {
  describe('Affectations Management Update Component', () => {
    let comp: AffectationsUpdateComponent;
    let fixture: ComponentFixture<AffectationsUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let affectationsService: AffectationsService;
    let equipementService: EquipementService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AffectationsUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AffectationsUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AffectationsUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      affectationsService = TestBed.inject(AffectationsService);
      equipementService = TestBed.inject(EquipementService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Equipement query and add missing value', () => {
        const affectations: IAffectations = { id: 456 };
        const equipement: IEquipement = { id: 10393 };
        affectations.equipement = equipement;

        const equipementCollection: IEquipement[] = [{ id: 88965 }];
        spyOn(equipementService, 'query').and.returnValue(of(new HttpResponse({ body: equipementCollection })));
        const additionalEquipements = [equipement];
        const expectedCollection: IEquipement[] = [...additionalEquipements, ...equipementCollection];
        spyOn(equipementService, 'addEquipementToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ affectations });
        comp.ngOnInit();

        expect(equipementService.query).toHaveBeenCalled();
        expect(equipementService.addEquipementToCollectionIfMissing).toHaveBeenCalledWith(equipementCollection, ...additionalEquipements);
        expect(comp.equipementsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const affectations: IAffectations = { id: 456 };
        const equipement: IEquipement = { id: 94030 };
        affectations.equipement = equipement;

        activatedRoute.data = of({ affectations });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(affectations));
        expect(comp.equipementsSharedCollection).toContain(equipement);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const affectations = { id: 123 };
        spyOn(affectationsService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ affectations });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: affectations }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(affectationsService.update).toHaveBeenCalledWith(affectations);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const affectations = new Affectations();
        spyOn(affectationsService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ affectations });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: affectations }));
        saveSubject.complete();

        // THEN
        expect(affectationsService.create).toHaveBeenCalledWith(affectations);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const affectations = { id: 123 };
        spyOn(affectationsService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ affectations });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(affectationsService.update).toHaveBeenCalledWith(affectations);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackEquipementById', () => {
        it('Should return tracked Equipement primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackEquipementById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
