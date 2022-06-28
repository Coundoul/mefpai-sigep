jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CategorieMatiereService } from '../service/categorie-matiere.service';
import { ICategorieMatiere, CategorieMatiere } from '../categorie-matiere.model';

import { CategorieMatiereUpdateComponent } from './categorie-matiere-update.component';

describe('Component Tests', () => {
  describe('CategorieMatiere Management Update Component', () => {
    let comp: CategorieMatiereUpdateComponent;
    let fixture: ComponentFixture<CategorieMatiereUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let categorieMatiereService: CategorieMatiereService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CategorieMatiereUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CategorieMatiereUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CategorieMatiereUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      categorieMatiereService = TestBed.inject(CategorieMatiereService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const categorieMatiere: ICategorieMatiere = { id: 456 };

        activatedRoute.data = of({ categorieMatiere });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(categorieMatiere));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const categorieMatiere = { id: 123 };
        spyOn(categorieMatiereService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ categorieMatiere });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: categorieMatiere }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(categorieMatiereService.update).toHaveBeenCalledWith(categorieMatiere);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const categorieMatiere = new CategorieMatiere();
        spyOn(categorieMatiereService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ categorieMatiere });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: categorieMatiere }));
        saveSubject.complete();

        // THEN
        expect(categorieMatiereService.create).toHaveBeenCalledWith(categorieMatiere);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const categorieMatiere = { id: 123 };
        spyOn(categorieMatiereService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ categorieMatiere });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(categorieMatiereService.update).toHaveBeenCalledWith(categorieMatiere);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
