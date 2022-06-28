jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DirecteurService } from '../service/directeur.service';
import { IDirecteur, Directeur } from '../directeur.model';

import { DirecteurUpdateComponent } from './directeur-update.component';

describe('Component Tests', () => {
  describe('Directeur Management Update Component', () => {
    let comp: DirecteurUpdateComponent;
    let fixture: ComponentFixture<DirecteurUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let directeurService: DirecteurService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DirecteurUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DirecteurUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DirecteurUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      directeurService = TestBed.inject(DirecteurService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const directeur: IDirecteur = { id: 456 };

        activatedRoute.data = of({ directeur });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(directeur));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const directeur = { id: 123 };
        spyOn(directeurService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ directeur });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: directeur }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(directeurService.update).toHaveBeenCalledWith(directeur);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const directeur = new Directeur();
        spyOn(directeurService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ directeur });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: directeur }));
        saveSubject.complete();

        // THEN
        expect(directeurService.create).toHaveBeenCalledWith(directeur);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const directeur = { id: 123 };
        spyOn(directeurService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ directeur });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(directeurService.update).toHaveBeenCalledWith(directeur);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
