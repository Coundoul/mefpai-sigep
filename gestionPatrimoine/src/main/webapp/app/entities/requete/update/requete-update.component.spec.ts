jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { RequeteService } from '../service/requete.service';
import { IRequete, Requete } from '../requete.model';
import { IBureau } from 'app/entities/bureau/bureau.model';
import { BureauService } from 'app/entities/bureau/service/bureau.service';

import { RequeteUpdateComponent } from './requete-update.component';

describe('Component Tests', () => {
  describe('Requete Management Update Component', () => {
    let comp: RequeteUpdateComponent;
    let fixture: ComponentFixture<RequeteUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let requeteService: RequeteService;
    let bureauService: BureauService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [RequeteUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(RequeteUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RequeteUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      requeteService = TestBed.inject(RequeteService);
      bureauService = TestBed.inject(BureauService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Bureau query and add missing value', () => {
        const requete: IRequete = { id: 456 };
        const nomStructure: IBureau = { id: 8253 };
        requete.nomStructure = nomStructure;

        const bureauCollection: IBureau[] = [{ id: 49100 }];
        spyOn(bureauService, 'query').and.returnValue(of(new HttpResponse({ body: bureauCollection })));
        const additionalBureaus = [nomStructure];
        const expectedCollection: IBureau[] = [...additionalBureaus, ...bureauCollection];
        spyOn(bureauService, 'addBureauToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ requete });
        comp.ngOnInit();

        expect(bureauService.query).toHaveBeenCalled();
        expect(bureauService.addBureauToCollectionIfMissing).toHaveBeenCalledWith(bureauCollection, ...additionalBureaus);
        expect(comp.bureausSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const requete: IRequete = { id: 456 };
        const nomStructure: IBureau = { id: 49267 };
        requete.nomStructure = nomStructure;

        activatedRoute.data = of({ requete });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(requete));
        expect(comp.bureausSharedCollection).toContain(nomStructure);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const requete = { id: 123 };
        spyOn(requeteService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ requete });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: requete }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(requeteService.update).toHaveBeenCalledWith(requete);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const requete = new Requete();
        spyOn(requeteService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ requete });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: requete }));
        saveSubject.complete();

        // THEN
        expect(requeteService.create).toHaveBeenCalledWith(requete);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const requete = { id: 123 };
        spyOn(requeteService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ requete });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(requeteService.update).toHaveBeenCalledWith(requete);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackBureauById', () => {
        it('Should return tracked Bureau primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackBureauById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
