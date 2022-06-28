jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { UtilisateurFinalService } from '../service/utilisateur-final.service';
import { IUtilisateurFinal, UtilisateurFinal } from '../utilisateur-final.model';

import { UtilisateurFinalUpdateComponent } from './utilisateur-final-update.component';

describe('Component Tests', () => {
  describe('UtilisateurFinal Management Update Component', () => {
    let comp: UtilisateurFinalUpdateComponent;
    let fixture: ComponentFixture<UtilisateurFinalUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let utilisateurFinalService: UtilisateurFinalService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [UtilisateurFinalUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(UtilisateurFinalUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UtilisateurFinalUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      utilisateurFinalService = TestBed.inject(UtilisateurFinalService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const utilisateurFinal: IUtilisateurFinal = { id: 456 };

        activatedRoute.data = of({ utilisateurFinal });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(utilisateurFinal));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const utilisateurFinal = { id: 123 };
        spyOn(utilisateurFinalService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ utilisateurFinal });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: utilisateurFinal }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(utilisateurFinalService.update).toHaveBeenCalledWith(utilisateurFinal);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const utilisateurFinal = new UtilisateurFinal();
        spyOn(utilisateurFinalService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ utilisateurFinal });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: utilisateurFinal }));
        saveSubject.complete();

        // THEN
        expect(utilisateurFinalService.create).toHaveBeenCalledWith(utilisateurFinal);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const utilisateurFinal = { id: 123 };
        spyOn(utilisateurFinalService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ utilisateurFinal });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(utilisateurFinalService.update).toHaveBeenCalledWith(utilisateurFinal);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
