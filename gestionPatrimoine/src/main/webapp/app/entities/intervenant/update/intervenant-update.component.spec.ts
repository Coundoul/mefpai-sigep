jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { IntervenantService } from '../service/intervenant.service';
import { IIntervenant, Intervenant } from '../intervenant.model';
import { IProjets } from 'app/entities/projets/projets.model';
import { ProjetsService } from 'app/entities/projets/service/projets.service';

import { IntervenantUpdateComponent } from './intervenant-update.component';

describe('Component Tests', () => {
  describe('Intervenant Management Update Component', () => {
    let comp: IntervenantUpdateComponent;
    let fixture: ComponentFixture<IntervenantUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let intervenantService: IntervenantService;
    let projetsService: ProjetsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [IntervenantUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(IntervenantUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(IntervenantUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      intervenantService = TestBed.inject(IntervenantService);
      projetsService = TestBed.inject(ProjetsService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Projets query and add missing value', () => {
        const intervenant: IIntervenant = { id: 456 };
        const nomProjet: IProjets = { id: 81106 };
        intervenant.nomProjet = nomProjet;

        const projetsCollection: IProjets[] = [{ id: 25891 }];
        spyOn(projetsService, 'query').and.returnValue(of(new HttpResponse({ body: projetsCollection })));
        const additionalProjets = [nomProjet];
        const expectedCollection: IProjets[] = [...additionalProjets, ...projetsCollection];
        spyOn(projetsService, 'addProjetsToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ intervenant });
        comp.ngOnInit();

        expect(projetsService.query).toHaveBeenCalled();
        expect(projetsService.addProjetsToCollectionIfMissing).toHaveBeenCalledWith(projetsCollection, ...additionalProjets);
        expect(comp.projetsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const intervenant: IIntervenant = { id: 456 };
        const nomProjet: IProjets = { id: 5948 };
        intervenant.nomProjet = nomProjet;

        activatedRoute.data = of({ intervenant });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(intervenant));
        expect(comp.projetsSharedCollection).toContain(nomProjet);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const intervenant = { id: 123 };
        spyOn(intervenantService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ intervenant });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: intervenant }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(intervenantService.update).toHaveBeenCalledWith(intervenant);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const intervenant = new Intervenant();
        spyOn(intervenantService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ intervenant });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: intervenant }));
        saveSubject.complete();

        // THEN
        expect(intervenantService.create).toHaveBeenCalledWith(intervenant);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const intervenant = { id: 123 };
        spyOn(intervenantService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ intervenant });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(intervenantService.update).toHaveBeenCalledWith(intervenant);
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
