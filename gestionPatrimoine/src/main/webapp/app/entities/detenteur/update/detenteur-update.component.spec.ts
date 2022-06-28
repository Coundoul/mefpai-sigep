jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DetenteurService } from '../service/detenteur.service';
import { IDetenteur, Detenteur } from '../detenteur.model';

import { DetenteurUpdateComponent } from './detenteur-update.component';

describe('Component Tests', () => {
  describe('Detenteur Management Update Component', () => {
    let comp: DetenteurUpdateComponent;
    let fixture: ComponentFixture<DetenteurUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let detenteurService: DetenteurService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DetenteurUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DetenteurUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DetenteurUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      detenteurService = TestBed.inject(DetenteurService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const detenteur: IDetenteur = { id: 456 };

        activatedRoute.data = of({ detenteur });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(detenteur));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const detenteur = { id: 123 };
        spyOn(detenteurService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ detenteur });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: detenteur }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(detenteurService.update).toHaveBeenCalledWith(detenteur);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const detenteur = new Detenteur();
        spyOn(detenteurService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ detenteur });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: detenteur }));
        saveSubject.complete();

        // THEN
        expect(detenteurService.create).toHaveBeenCalledWith(detenteur);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const detenteur = { id: 123 };
        spyOn(detenteurService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ detenteur });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(detenteurService.update).toHaveBeenCalledWith(detenteur);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
