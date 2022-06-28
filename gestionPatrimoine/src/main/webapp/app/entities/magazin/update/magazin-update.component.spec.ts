jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { MagazinService } from '../service/magazin.service';
import { IMagazin, Magazin } from '../magazin.model';
import { IQuartier } from 'app/entities/quartier/quartier.model';
import { QuartierService } from 'app/entities/quartier/service/quartier.service';

import { MagazinUpdateComponent } from './magazin-update.component';

describe('Component Tests', () => {
  describe('Magazin Management Update Component', () => {
    let comp: MagazinUpdateComponent;
    let fixture: ComponentFixture<MagazinUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let magazinService: MagazinService;
    let quartierService: QuartierService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MagazinUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(MagazinUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MagazinUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      magazinService = TestBed.inject(MagazinService);
      quartierService = TestBed.inject(QuartierService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Quartier query and add missing value', () => {
        const magazin: IMagazin = { id: 456 };
        const nomQuartier: IQuartier = { id: 52797 };
        magazin.nomQuartier = nomQuartier;

        const quartierCollection: IQuartier[] = [{ id: 33699 }];
        spyOn(quartierService, 'query').and.returnValue(of(new HttpResponse({ body: quartierCollection })));
        const additionalQuartiers = [nomQuartier];
        const expectedCollection: IQuartier[] = [...additionalQuartiers, ...quartierCollection];
        spyOn(quartierService, 'addQuartierToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ magazin });
        comp.ngOnInit();

        expect(quartierService.query).toHaveBeenCalled();
        expect(quartierService.addQuartierToCollectionIfMissing).toHaveBeenCalledWith(quartierCollection, ...additionalQuartiers);
        expect(comp.quartiersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const magazin: IMagazin = { id: 456 };
        const nomQuartier: IQuartier = { id: 54733 };
        magazin.nomQuartier = nomQuartier;

        activatedRoute.data = of({ magazin });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(magazin));
        expect(comp.quartiersSharedCollection).toContain(nomQuartier);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const magazin = { id: 123 };
        spyOn(magazinService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ magazin });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: magazin }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(magazinService.update).toHaveBeenCalledWith(magazin);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const magazin = new Magazin();
        spyOn(magazinService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ magazin });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: magazin }));
        saveSubject.complete();

        // THEN
        expect(magazinService.create).toHaveBeenCalledWith(magazin);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const magazin = { id: 123 };
        spyOn(magazinService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ magazin });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(magazinService.update).toHaveBeenCalledWith(magazin);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackQuartierById', () => {
        it('Should return tracked Quartier primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackQuartierById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
