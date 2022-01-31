jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { OrdonnaceurMatiereService } from '../service/ordonnaceur-matiere.service';
import { IOrdonnaceurMatiere, OrdonnaceurMatiere } from '../ordonnaceur-matiere.model';

import { OrdonnaceurMatiereUpdateComponent } from './ordonnaceur-matiere-update.component';

describe('Component Tests', () => {
  describe('OrdonnaceurMatiere Management Update Component', () => {
    let comp: OrdonnaceurMatiereUpdateComponent;
    let fixture: ComponentFixture<OrdonnaceurMatiereUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let ordonnaceurMatiereService: OrdonnaceurMatiereService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [OrdonnaceurMatiereUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(OrdonnaceurMatiereUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(OrdonnaceurMatiereUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      ordonnaceurMatiereService = TestBed.inject(OrdonnaceurMatiereService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const ordonnaceurMatiere: IOrdonnaceurMatiere = { id: 456 };

        activatedRoute.data = of({ ordonnaceurMatiere });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(ordonnaceurMatiere));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const ordonnaceurMatiere = { id: 123 };
        spyOn(ordonnaceurMatiereService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ ordonnaceurMatiere });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: ordonnaceurMatiere }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(ordonnaceurMatiereService.update).toHaveBeenCalledWith(ordonnaceurMatiere);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const ordonnaceurMatiere = new OrdonnaceurMatiere();
        spyOn(ordonnaceurMatiereService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ ordonnaceurMatiere });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: ordonnaceurMatiere }));
        saveSubject.complete();

        // THEN
        expect(ordonnaceurMatiereService.create).toHaveBeenCalledWith(ordonnaceurMatiere);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const ordonnaceurMatiere = { id: 123 };
        spyOn(ordonnaceurMatiereService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ ordonnaceurMatiere });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(ordonnaceurMatiereService.update).toHaveBeenCalledWith(ordonnaceurMatiere);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
