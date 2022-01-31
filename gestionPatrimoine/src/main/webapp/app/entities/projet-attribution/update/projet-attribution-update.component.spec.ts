jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ProjetAttributionService } from '../service/projet-attribution.service';
import { IProjetAttribution, ProjetAttribution } from '../projet-attribution.model';
import { IProjets } from 'app/entities/projets/projets.model';
import { ProjetsService } from 'app/entities/projets/service/projets.service';

import { ProjetAttributionUpdateComponent } from './projet-attribution-update.component';

describe('Component Tests', () => {
  describe('ProjetAttribution Management Update Component', () => {
    let comp: ProjetAttributionUpdateComponent;
    let fixture: ComponentFixture<ProjetAttributionUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let projetAttributionService: ProjetAttributionService;
    let projetsService: ProjetsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ProjetAttributionUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ProjetAttributionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProjetAttributionUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      projetAttributionService = TestBed.inject(ProjetAttributionService);
      projetsService = TestBed.inject(ProjetsService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Projets query and add missing value', () => {
        const projetAttribution: IProjetAttribution = { id: 456 };
        const nomProjet: IProjets = { id: 55987 };
        projetAttribution.nomProjet = nomProjet;

        const projetsCollection: IProjets[] = [{ id: 31538 }];
        spyOn(projetsService, 'query').and.returnValue(of(new HttpResponse({ body: projetsCollection })));
        const additionalProjets = [nomProjet];
        const expectedCollection: IProjets[] = [...additionalProjets, ...projetsCollection];
        spyOn(projetsService, 'addProjetsToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ projetAttribution });
        comp.ngOnInit();

        expect(projetsService.query).toHaveBeenCalled();
        expect(projetsService.addProjetsToCollectionIfMissing).toHaveBeenCalledWith(projetsCollection, ...additionalProjets);
        expect(comp.projetsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const projetAttribution: IProjetAttribution = { id: 456 };
        const nomProjet: IProjets = { id: 40347 };
        projetAttribution.nomProjet = nomProjet;

        activatedRoute.data = of({ projetAttribution });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(projetAttribution));
        expect(comp.projetsSharedCollection).toContain(nomProjet);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const projetAttribution = { id: 123 };
        spyOn(projetAttributionService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ projetAttribution });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: projetAttribution }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(projetAttributionService.update).toHaveBeenCalledWith(projetAttribution);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const projetAttribution = new ProjetAttribution();
        spyOn(projetAttributionService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ projetAttribution });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: projetAttribution }));
        saveSubject.complete();

        // THEN
        expect(projetAttributionService.create).toHaveBeenCalledWith(projetAttribution);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const projetAttribution = { id: 123 };
        spyOn(projetAttributionService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ projetAttribution });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(projetAttributionService.update).toHaveBeenCalledWith(projetAttribution);
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
