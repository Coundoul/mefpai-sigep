jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { IntendantService } from '../service/intendant.service';
import { IIntendant, Intendant } from '../intendant.model';

import { IntendantUpdateComponent } from './intendant-update.component';

describe('Component Tests', () => {
  describe('Intendant Management Update Component', () => {
    let comp: IntendantUpdateComponent;
    let fixture: ComponentFixture<IntendantUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let intendantService: IntendantService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [IntendantUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(IntendantUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(IntendantUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      intendantService = TestBed.inject(IntendantService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const intendant: IIntendant = { id: 456 };

        activatedRoute.data = of({ intendant });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(intendant));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const intendant = { id: 123 };
        spyOn(intendantService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ intendant });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: intendant }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(intendantService.update).toHaveBeenCalledWith(intendant);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const intendant = new Intendant();
        spyOn(intendantService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ intendant });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: intendant }));
        saveSubject.complete();

        // THEN
        expect(intendantService.create).toHaveBeenCalledWith(intendant);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const intendant = { id: 123 };
        spyOn(intendantService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ intendant });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(intendantService.update).toHaveBeenCalledWith(intendant);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
