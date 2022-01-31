jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ResponsableService } from '../service/responsable.service';
import { IResponsable, Responsable } from '../responsable.model';

import { ResponsableUpdateComponent } from './responsable-update.component';

describe('Component Tests', () => {
  describe('Responsable Management Update Component', () => {
    let comp: ResponsableUpdateComponent;
    let fixture: ComponentFixture<ResponsableUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let responsableService: ResponsableService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ResponsableUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ResponsableUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ResponsableUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      responsableService = TestBed.inject(ResponsableService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const responsable: IResponsable = { id: 456 };

        activatedRoute.data = of({ responsable });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(responsable));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const responsable = { id: 123 };
        spyOn(responsableService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ responsable });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: responsable }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(responsableService.update).toHaveBeenCalledWith(responsable);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const responsable = new Responsable();
        spyOn(responsableService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ responsable });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: responsable }));
        saveSubject.complete();

        // THEN
        expect(responsableService.create).toHaveBeenCalledWith(responsable);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const responsable = { id: 123 };
        spyOn(responsableService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ responsable });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(responsableService.update).toHaveBeenCalledWith(responsable);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
