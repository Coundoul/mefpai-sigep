jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TechnicienService } from '../service/technicien.service';
import { ITechnicien, Technicien } from '../technicien.model';
import { IChefMaintenance } from 'app/entities/chef-maintenance/chef-maintenance.model';
import { ChefMaintenanceService } from 'app/entities/chef-maintenance/service/chef-maintenance.service';

import { TechnicienUpdateComponent } from './technicien-update.component';

describe('Component Tests', () => {
  describe('Technicien Management Update Component', () => {
    let comp: TechnicienUpdateComponent;
    let fixture: ComponentFixture<TechnicienUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let technicienService: TechnicienService;
    let chefMaintenanceService: ChefMaintenanceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TechnicienUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TechnicienUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TechnicienUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      technicienService = TestBed.inject(TechnicienService);
      chefMaintenanceService = TestBed.inject(ChefMaintenanceService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call ChefMaintenance query and add missing value', () => {
        const technicien: ITechnicien = { id: 456 };
        const chefMaintenance: IChefMaintenance = { id: 5466 };
        technicien.chefMaintenance = chefMaintenance;

        const chefMaintenanceCollection: IChefMaintenance[] = [{ id: 60306 }];
        spyOn(chefMaintenanceService, 'query').and.returnValue(of(new HttpResponse({ body: chefMaintenanceCollection })));
        const additionalChefMaintenances = [chefMaintenance];
        const expectedCollection: IChefMaintenance[] = [...additionalChefMaintenances, ...chefMaintenanceCollection];
        spyOn(chefMaintenanceService, 'addChefMaintenanceToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ technicien });
        comp.ngOnInit();

        expect(chefMaintenanceService.query).toHaveBeenCalled();
        expect(chefMaintenanceService.addChefMaintenanceToCollectionIfMissing).toHaveBeenCalledWith(
          chefMaintenanceCollection,
          ...additionalChefMaintenances
        );
        expect(comp.chefMaintenancesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const technicien: ITechnicien = { id: 456 };
        const chefMaintenance: IChefMaintenance = { id: 28775 };
        technicien.chefMaintenance = chefMaintenance;

        activatedRoute.data = of({ technicien });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(technicien));
        expect(comp.chefMaintenancesSharedCollection).toContain(chefMaintenance);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const technicien = { id: 123 };
        spyOn(technicienService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ technicien });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: technicien }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(technicienService.update).toHaveBeenCalledWith(technicien);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const technicien = new Technicien();
        spyOn(technicienService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ technicien });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: technicien }));
        saveSubject.complete();

        // THEN
        expect(technicienService.create).toHaveBeenCalledWith(technicien);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const technicien = { id: 123 };
        spyOn(technicienService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ technicien });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(technicienService.update).toHaveBeenCalledWith(technicien);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackChefMaintenanceById', () => {
        it('Should return tracked ChefMaintenance primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackChefMaintenanceById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
