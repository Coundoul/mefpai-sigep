jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CorpsEtatService } from '../service/corps-etat.service';
import { ICorpsEtat, CorpsEtat } from '../corps-etat.model';
import { IResponsable } from 'app/entities/responsable/responsable.model';
import { ResponsableService } from 'app/entities/responsable/service/responsable.service';

import { CorpsEtatUpdateComponent } from './corps-etat-update.component';

describe('Component Tests', () => {
  describe('CorpsEtat Management Update Component', () => {
    let comp: CorpsEtatUpdateComponent;
    let fixture: ComponentFixture<CorpsEtatUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let corpsEtatService: CorpsEtatService;
    let responsableService: ResponsableService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CorpsEtatUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CorpsEtatUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CorpsEtatUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      corpsEtatService = TestBed.inject(CorpsEtatService);
      responsableService = TestBed.inject(ResponsableService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Responsable query and add missing value', () => {
        const corpsEtat: ICorpsEtat = { id: 456 };
        const nomResponsable: IResponsable = { id: 11048 };
        corpsEtat.nomResponsable = nomResponsable;

        const responsableCollection: IResponsable[] = [{ id: 86136 }];
        spyOn(responsableService, 'query').and.returnValue(of(new HttpResponse({ body: responsableCollection })));
        const additionalResponsables = [nomResponsable];
        const expectedCollection: IResponsable[] = [...additionalResponsables, ...responsableCollection];
        spyOn(responsableService, 'addResponsableToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ corpsEtat });
        comp.ngOnInit();

        expect(responsableService.query).toHaveBeenCalled();
        expect(responsableService.addResponsableToCollectionIfMissing).toHaveBeenCalledWith(
          responsableCollection,
          ...additionalResponsables
        );
        expect(comp.responsablesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const corpsEtat: ICorpsEtat = { id: 456 };
        const nomResponsable: IResponsable = { id: 89964 };
        corpsEtat.nomResponsable = nomResponsable;

        activatedRoute.data = of({ corpsEtat });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(corpsEtat));
        expect(comp.responsablesSharedCollection).toContain(nomResponsable);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const corpsEtat = { id: 123 };
        spyOn(corpsEtatService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ corpsEtat });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: corpsEtat }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(corpsEtatService.update).toHaveBeenCalledWith(corpsEtat);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const corpsEtat = new CorpsEtat();
        spyOn(corpsEtatService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ corpsEtat });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: corpsEtat }));
        saveSubject.complete();

        // THEN
        expect(corpsEtatService.create).toHaveBeenCalledWith(corpsEtat);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const corpsEtat = { id: 123 };
        spyOn(corpsEtatService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ corpsEtat });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(corpsEtatService.update).toHaveBeenCalledWith(corpsEtat);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackResponsableById', () => {
        it('Should return tracked Responsable primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackResponsableById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
