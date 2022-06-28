jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { QuartierService } from '../service/quartier.service';
import { IQuartier, Quartier } from '../quartier.model';
import { ICommune } from 'app/entities/commune/commune.model';
import { CommuneService } from 'app/entities/commune/service/commune.service';

import { QuartierUpdateComponent } from './quartier-update.component';

describe('Component Tests', () => {
  describe('Quartier Management Update Component', () => {
    let comp: QuartierUpdateComponent;
    let fixture: ComponentFixture<QuartierUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let quartierService: QuartierService;
    let communeService: CommuneService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [QuartierUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(QuartierUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(QuartierUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      quartierService = TestBed.inject(QuartierService);
      communeService = TestBed.inject(CommuneService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Commune query and add missing value', () => {
        const quartier: IQuartier = { id: 456 };
        const nomCommune: ICommune = { id: 67008 };
        quartier.nomCommune = nomCommune;

        const communeCollection: ICommune[] = [{ id: 39985 }];
        spyOn(communeService, 'query').and.returnValue(of(new HttpResponse({ body: communeCollection })));
        const additionalCommunes = [nomCommune];
        const expectedCollection: ICommune[] = [...additionalCommunes, ...communeCollection];
        spyOn(communeService, 'addCommuneToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ quartier });
        comp.ngOnInit();

        expect(communeService.query).toHaveBeenCalled();
        expect(communeService.addCommuneToCollectionIfMissing).toHaveBeenCalledWith(communeCollection, ...additionalCommunes);
        expect(comp.communesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const quartier: IQuartier = { id: 456 };
        const nomCommune: ICommune = { id: 53523 };
        quartier.nomCommune = nomCommune;

        activatedRoute.data = of({ quartier });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(quartier));
        expect(comp.communesSharedCollection).toContain(nomCommune);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const quartier = { id: 123 };
        spyOn(quartierService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ quartier });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: quartier }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(quartierService.update).toHaveBeenCalledWith(quartier);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const quartier = new Quartier();
        spyOn(quartierService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ quartier });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: quartier }));
        saveSubject.complete();

        // THEN
        expect(quartierService.create).toHaveBeenCalledWith(quartier);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const quartier = { id: 123 };
        spyOn(quartierService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ quartier });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(quartierService.update).toHaveBeenCalledWith(quartier);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCommuneById', () => {
        it('Should return tracked Commune primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCommuneById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
