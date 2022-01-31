jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ChefProjetService } from '../service/chef-projet.service';
import { IChefProjet, ChefProjet } from '../chef-projet.model';

import { ChefProjetUpdateComponent } from './chef-projet-update.component';

describe('Component Tests', () => {
  describe('ChefProjet Management Update Component', () => {
    let comp: ChefProjetUpdateComponent;
    let fixture: ComponentFixture<ChefProjetUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let chefProjetService: ChefProjetService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ChefProjetUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ChefProjetUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ChefProjetUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      chefProjetService = TestBed.inject(ChefProjetService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const chefProjet: IChefProjet = { id: 456 };

        activatedRoute.data = of({ chefProjet });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(chefProjet));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const chefProjet = { id: 123 };
        spyOn(chefProjetService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ chefProjet });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: chefProjet }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(chefProjetService.update).toHaveBeenCalledWith(chefProjet);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const chefProjet = new ChefProjet();
        spyOn(chefProjetService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ chefProjet });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: chefProjet }));
        saveSubject.complete();

        // THEN
        expect(chefProjetService.create).toHaveBeenCalledWith(chefProjet);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const chefProjet = { id: 123 };
        spyOn(chefProjetService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ chefProjet });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(chefProjetService.update).toHaveBeenCalledWith(chefProjet);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
