jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { EtapesService } from '../service/etapes.service';
import { IEtapes, Etapes } from '../etapes.model';
import { IProjets } from 'app/entities/projets/projets.model';
import { ProjetsService } from 'app/entities/projets/service/projets.service';

import { EtapesUpdateComponent } from './etapes-update.component';

describe('Component Tests', () => {
  describe('Etapes Management Update Component', () => {
    let comp: EtapesUpdateComponent;
    let fixture: ComponentFixture<EtapesUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let etapesService: EtapesService;
    let projetsService: ProjetsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EtapesUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(EtapesUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EtapesUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      etapesService = TestBed.inject(EtapesService);
      projetsService = TestBed.inject(ProjetsService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Projets query and add missing value', () => {
        const etapes: IEtapes = { id: 456 };
        const nomProjet: IProjets = { id: 64321 };
        etapes.nomProjet = nomProjet;

        const projetsCollection: IProjets[] = [{ id: 75042 }];
        spyOn(projetsService, 'query').and.returnValue(of(new HttpResponse({ body: projetsCollection })));
        const additionalProjets = [nomProjet];
        const expectedCollection: IProjets[] = [...additionalProjets, ...projetsCollection];
        spyOn(projetsService, 'addProjetsToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ etapes });
        comp.ngOnInit();

        expect(projetsService.query).toHaveBeenCalled();
        expect(projetsService.addProjetsToCollectionIfMissing).toHaveBeenCalledWith(projetsCollection, ...additionalProjets);
        expect(comp.projetsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const etapes: IEtapes = { id: 456 };
        const nomProjet: IProjets = { id: 33163 };
        etapes.nomProjet = nomProjet;

        activatedRoute.data = of({ etapes });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(etapes));
        expect(comp.projetsSharedCollection).toContain(nomProjet);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const etapes = { id: 123 };
        spyOn(etapesService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ etapes });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: etapes }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(etapesService.update).toHaveBeenCalledWith(etapes);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const etapes = new Etapes();
        spyOn(etapesService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ etapes });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: etapes }));
        saveSubject.complete();

        // THEN
        expect(etapesService.create).toHaveBeenCalledWith(etapes);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const etapes = { id: 123 };
        spyOn(etapesService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ etapes });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(etapesService.update).toHaveBeenCalledWith(etapes);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackProjetsById', () => {
        it('Should return tracked Projets primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackProjetsById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
