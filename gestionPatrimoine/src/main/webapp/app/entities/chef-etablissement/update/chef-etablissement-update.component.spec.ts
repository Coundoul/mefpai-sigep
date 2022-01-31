jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ChefEtablissementService } from '../service/chef-etablissement.service';
import { IChefEtablissement, ChefEtablissement } from '../chef-etablissement.model';

import { ChefEtablissementUpdateComponent } from './chef-etablissement-update.component';

describe('Component Tests', () => {
  describe('ChefEtablissement Management Update Component', () => {
    let comp: ChefEtablissementUpdateComponent;
    let fixture: ComponentFixture<ChefEtablissementUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let chefEtablissementService: ChefEtablissementService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ChefEtablissementUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ChefEtablissementUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ChefEtablissementUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      chefEtablissementService = TestBed.inject(ChefEtablissementService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const chefEtablissement: IChefEtablissement = { id: 456 };

        activatedRoute.data = of({ chefEtablissement });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(chefEtablissement));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const chefEtablissement = { id: 123 };
        spyOn(chefEtablissementService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ chefEtablissement });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: chefEtablissement }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(chefEtablissementService.update).toHaveBeenCalledWith(chefEtablissement);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const chefEtablissement = new ChefEtablissement();
        spyOn(chefEtablissementService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ chefEtablissement });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: chefEtablissement }));
        saveSubject.complete();

        // THEN
        expect(chefEtablissementService.create).toHaveBeenCalledWith(chefEtablissement);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const chefEtablissement = { id: 123 };
        spyOn(chefEtablissementService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ chefEtablissement });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(chefEtablissementService.update).toHaveBeenCalledWith(chefEtablissement);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
