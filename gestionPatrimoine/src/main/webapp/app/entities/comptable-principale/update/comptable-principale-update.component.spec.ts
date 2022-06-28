jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ComptablePrincipaleService } from '../service/comptable-principale.service';
import { IComptablePrincipale, ComptablePrincipale } from '../comptable-principale.model';

import { ComptablePrincipaleUpdateComponent } from './comptable-principale-update.component';

describe('Component Tests', () => {
  describe('ComptablePrincipale Management Update Component', () => {
    let comp: ComptablePrincipaleUpdateComponent;
    let fixture: ComponentFixture<ComptablePrincipaleUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let comptablePrincipaleService: ComptablePrincipaleService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ComptablePrincipaleUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ComptablePrincipaleUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ComptablePrincipaleUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      comptablePrincipaleService = TestBed.inject(ComptablePrincipaleService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const comptablePrincipale: IComptablePrincipale = { id: 456 };

        activatedRoute.data = of({ comptablePrincipale });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(comptablePrincipale));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const comptablePrincipale = { id: 123 };
        spyOn(comptablePrincipaleService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ comptablePrincipale });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: comptablePrincipale }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(comptablePrincipaleService.update).toHaveBeenCalledWith(comptablePrincipale);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const comptablePrincipale = new ComptablePrincipale();
        spyOn(comptablePrincipaleService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ comptablePrincipale });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: comptablePrincipale }));
        saveSubject.complete();

        // THEN
        expect(comptablePrincipaleService.create).toHaveBeenCalledWith(comptablePrincipale);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const comptablePrincipale = { id: 123 };
        spyOn(comptablePrincipaleService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ comptablePrincipale });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(comptablePrincipaleService.update).toHaveBeenCalledWith(comptablePrincipale);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
