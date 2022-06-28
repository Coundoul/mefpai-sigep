jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DepartementService } from '../service/departement.service';
import { IDepartement, Departement } from '../departement.model';
import { IRegion } from 'app/entities/region/region.model';
import { RegionService } from 'app/entities/region/service/region.service';

import { DepartementUpdateComponent } from './departement-update.component';

describe('Component Tests', () => {
  describe('Departement Management Update Component', () => {
    let comp: DepartementUpdateComponent;
    let fixture: ComponentFixture<DepartementUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let departementService: DepartementService;
    let regionService: RegionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DepartementUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DepartementUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DepartementUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      departementService = TestBed.inject(DepartementService);
      regionService = TestBed.inject(RegionService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Region query and add missing value', () => {
        const departement: IDepartement = { id: 456 };
        const nomRegion: IRegion = { id: 69097 };
        departement.nomRegion = nomRegion;

        const regionCollection: IRegion[] = [{ id: 39540 }];
        spyOn(regionService, 'query').and.returnValue(of(new HttpResponse({ body: regionCollection })));
        const additionalRegions = [nomRegion];
        const expectedCollection: IRegion[] = [...additionalRegions, ...regionCollection];
        spyOn(regionService, 'addRegionToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ departement });
        comp.ngOnInit();

        expect(regionService.query).toHaveBeenCalled();
        expect(regionService.addRegionToCollectionIfMissing).toHaveBeenCalledWith(regionCollection, ...additionalRegions);
        expect(comp.regionsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const departement: IDepartement = { id: 456 };
        const nomRegion: IRegion = { id: 31515 };
        departement.nomRegion = nomRegion;

        activatedRoute.data = of({ departement });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(departement));
        expect(comp.regionsSharedCollection).toContain(nomRegion);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const departement = { id: 123 };
        spyOn(departementService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ departement });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: departement }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(departementService.update).toHaveBeenCalledWith(departement);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const departement = new Departement();
        spyOn(departementService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ departement });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: departement }));
        saveSubject.complete();

        // THEN
        expect(departementService.create).toHaveBeenCalledWith(departement);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const departement = { id: 123 };
        spyOn(departementService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ departement });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(departementService.update).toHaveBeenCalledWith(departement);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackRegionById', () => {
        it('Should return tracked Region primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackRegionById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
