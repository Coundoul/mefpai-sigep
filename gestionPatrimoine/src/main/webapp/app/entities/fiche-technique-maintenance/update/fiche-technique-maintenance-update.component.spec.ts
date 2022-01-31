jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FicheTechniqueMaintenanceService } from '../service/fiche-technique-maintenance.service';
import { IFicheTechniqueMaintenance, FicheTechniqueMaintenance } from '../fiche-technique-maintenance.model';
import { IRequete } from 'app/entities/requete/requete.model';
import { RequeteService } from 'app/entities/requete/service/requete.service';

import { FicheTechniqueMaintenanceUpdateComponent } from './fiche-technique-maintenance-update.component';

describe('Component Tests', () => {
  describe('FicheTechniqueMaintenance Management Update Component', () => {
    let comp: FicheTechniqueMaintenanceUpdateComponent;
    let fixture: ComponentFixture<FicheTechniqueMaintenanceUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let ficheTechniqueMaintenanceService: FicheTechniqueMaintenanceService;
    let requeteService: RequeteService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FicheTechniqueMaintenanceUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(FicheTechniqueMaintenanceUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FicheTechniqueMaintenanceUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      ficheTechniqueMaintenanceService = TestBed.inject(FicheTechniqueMaintenanceService);
      requeteService = TestBed.inject(RequeteService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Requete query and add missing value', () => {
        const ficheTechniqueMaintenance: IFicheTechniqueMaintenance = { id: 456 };
        const type: IRequete = { id: 68591 };
        ficheTechniqueMaintenance.type = type;

        const requeteCollection: IRequete[] = [{ id: 41658 }];
        spyOn(requeteService, 'query').and.returnValue(of(new HttpResponse({ body: requeteCollection })));
        const additionalRequetes = [type];
        const expectedCollection: IRequete[] = [...additionalRequetes, ...requeteCollection];
        spyOn(requeteService, 'addRequeteToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ ficheTechniqueMaintenance });
        comp.ngOnInit();

        expect(requeteService.query).toHaveBeenCalled();
        expect(requeteService.addRequeteToCollectionIfMissing).toHaveBeenCalledWith(requeteCollection, ...additionalRequetes);
        expect(comp.requetesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const ficheTechniqueMaintenance: IFicheTechniqueMaintenance = { id: 456 };
        const type: IRequete = { id: 28219 };
        ficheTechniqueMaintenance.type = type;

        activatedRoute.data = of({ ficheTechniqueMaintenance });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(ficheTechniqueMaintenance));
        expect(comp.requetesSharedCollection).toContain(type);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const ficheTechniqueMaintenance = { id: 123 };
        spyOn(ficheTechniqueMaintenanceService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ ficheTechniqueMaintenance });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: ficheTechniqueMaintenance }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(ficheTechniqueMaintenanceService.update).toHaveBeenCalledWith(ficheTechniqueMaintenance);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const ficheTechniqueMaintenance = new FicheTechniqueMaintenance();
        spyOn(ficheTechniqueMaintenanceService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ ficheTechniqueMaintenance });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: ficheTechniqueMaintenance }));
        saveSubject.complete();

        // THEN
        expect(ficheTechniqueMaintenanceService.create).toHaveBeenCalledWith(ficheTechniqueMaintenance);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const ficheTechniqueMaintenance = { id: 123 };
        spyOn(ficheTechniqueMaintenanceService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ ficheTechniqueMaintenance });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(ficheTechniqueMaintenanceService.update).toHaveBeenCalledWith(ficheTechniqueMaintenance);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackRequeteById', () => {
        it('Should return tracked Requete primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackRequeteById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
