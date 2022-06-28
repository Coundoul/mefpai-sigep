jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { BonService } from '../service/bon.service';
import { IBon, Bon } from '../bon.model';

import { BonUpdateComponent } from './bon-update.component';

describe('Component Tests', () => {
  describe('Bon Management Update Component', () => {
    let comp: BonUpdateComponent;
    let fixture: ComponentFixture<BonUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let bonService: BonService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [BonUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(BonUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BonUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      bonService = TestBed.inject(BonService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const bon: IBon = { id: 456 };

        activatedRoute.data = of({ bon });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(bon));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const bon = { id: 123 };
        spyOn(bonService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ bon });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: bon }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(bonService.update).toHaveBeenCalledWith(bon);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const bon = new Bon();
        spyOn(bonService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ bon });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: bon }));
        saveSubject.complete();

        // THEN
        expect(bonService.create).toHaveBeenCalledWith(bon);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const bon = { id: 123 };
        spyOn(bonService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ bon });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(bonService.update).toHaveBeenCalledWith(bon);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
