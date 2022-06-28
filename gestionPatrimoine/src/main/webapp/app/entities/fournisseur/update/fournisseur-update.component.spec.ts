jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FournisseurService } from '../service/fournisseur.service';
import { IFournisseur, Fournisseur } from '../fournisseur.model';

import { FournisseurUpdateComponent } from './fournisseur-update.component';

describe('Component Tests', () => {
  describe('Fournisseur Management Update Component', () => {
    let comp: FournisseurUpdateComponent;
    let fixture: ComponentFixture<FournisseurUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let fournisseurService: FournisseurService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FournisseurUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(FournisseurUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FournisseurUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      fournisseurService = TestBed.inject(FournisseurService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const fournisseur: IFournisseur = { id: 456 };

        activatedRoute.data = of({ fournisseur });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(fournisseur));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const fournisseur = { id: 123 };
        spyOn(fournisseurService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ fournisseur });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: fournisseur }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(fournisseurService.update).toHaveBeenCalledWith(fournisseur);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const fournisseur = new Fournisseur();
        spyOn(fournisseurService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ fournisseur });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: fournisseur }));
        saveSubject.complete();

        // THEN
        expect(fournisseurService.create).toHaveBeenCalledWith(fournisseur);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const fournisseur = { id: 123 };
        spyOn(fournisseurService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ fournisseur });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(fournisseurService.update).toHaveBeenCalledWith(fournisseur);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
