jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ContratProjetService } from '../service/contrat-projet.service';
import { IContratProjet, ContratProjet } from '../contrat-projet.model';

import { ContratProjetUpdateComponent } from './contrat-projet-update.component';

describe('Component Tests', () => {
  describe('ContratProjet Management Update Component', () => {
    let comp: ContratProjetUpdateComponent;
    let fixture: ComponentFixture<ContratProjetUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let contratProjetService: ContratProjetService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ContratProjetUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ContratProjetUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ContratProjetUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      contratProjetService = TestBed.inject(ContratProjetService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const contratProjet: IContratProjet = { id: 456 };

        activatedRoute.data = of({ contratProjet });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(contratProjet));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const contratProjet = { id: 123 };
        spyOn(contratProjetService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ contratProjet });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: contratProjet }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(contratProjetService.update).toHaveBeenCalledWith(contratProjet);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const contratProjet = new ContratProjet();
        spyOn(contratProjetService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ contratProjet });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: contratProjet }));
        saveSubject.complete();

        // THEN
        expect(contratProjetService.create).toHaveBeenCalledWith(contratProjet);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const contratProjet = { id: 123 };
        spyOn(contratProjetService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ contratProjet });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(contratProjetService.update).toHaveBeenCalledWith(contratProjet);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
