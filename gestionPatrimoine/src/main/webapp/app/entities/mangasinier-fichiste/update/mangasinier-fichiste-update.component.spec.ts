jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { MangasinierFichisteService } from '../service/mangasinier-fichiste.service';
import { IMangasinierFichiste, MangasinierFichiste } from '../mangasinier-fichiste.model';
import { IComptablePrincipale } from 'app/entities/comptable-principale/comptable-principale.model';
import { ComptablePrincipaleService } from 'app/entities/comptable-principale/service/comptable-principale.service';

import { MangasinierFichisteUpdateComponent } from './mangasinier-fichiste-update.component';

describe('Component Tests', () => {
  describe('MangasinierFichiste Management Update Component', () => {
    let comp: MangasinierFichisteUpdateComponent;
    let fixture: ComponentFixture<MangasinierFichisteUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let mangasinierFichisteService: MangasinierFichisteService;
    let comptablePrincipaleService: ComptablePrincipaleService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MangasinierFichisteUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(MangasinierFichisteUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MangasinierFichisteUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      mangasinierFichisteService = TestBed.inject(MangasinierFichisteService);
      comptablePrincipaleService = TestBed.inject(ComptablePrincipaleService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call ComptablePrincipale query and add missing value', () => {
        const mangasinierFichiste: IMangasinierFichiste = { id: 456 };
        const comptablePrincipale: IComptablePrincipale = { id: 82068 };
        mangasinierFichiste.comptablePrincipale = comptablePrincipale;

        const comptablePrincipaleCollection: IComptablePrincipale[] = [{ id: 3479 }];
        spyOn(comptablePrincipaleService, 'query').and.returnValue(of(new HttpResponse({ body: comptablePrincipaleCollection })));
        const additionalComptablePrincipales = [comptablePrincipale];
        const expectedCollection: IComptablePrincipale[] = [...additionalComptablePrincipales, ...comptablePrincipaleCollection];
        spyOn(comptablePrincipaleService, 'addComptablePrincipaleToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ mangasinierFichiste });
        comp.ngOnInit();

        expect(comptablePrincipaleService.query).toHaveBeenCalled();
        expect(comptablePrincipaleService.addComptablePrincipaleToCollectionIfMissing).toHaveBeenCalledWith(
          comptablePrincipaleCollection,
          ...additionalComptablePrincipales
        );
        expect(comp.comptablePrincipalesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const mangasinierFichiste: IMangasinierFichiste = { id: 456 };
        const comptablePrincipale: IComptablePrincipale = { id: 44282 };
        mangasinierFichiste.comptablePrincipale = comptablePrincipale;

        activatedRoute.data = of({ mangasinierFichiste });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(mangasinierFichiste));
        expect(comp.comptablePrincipalesSharedCollection).toContain(comptablePrincipale);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const mangasinierFichiste = { id: 123 };
        spyOn(mangasinierFichisteService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ mangasinierFichiste });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: mangasinierFichiste }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(mangasinierFichisteService.update).toHaveBeenCalledWith(mangasinierFichiste);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const mangasinierFichiste = new MangasinierFichiste();
        spyOn(mangasinierFichisteService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ mangasinierFichiste });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: mangasinierFichiste }));
        saveSubject.complete();

        // THEN
        expect(mangasinierFichisteService.create).toHaveBeenCalledWith(mangasinierFichiste);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const mangasinierFichiste = { id: 123 };
        spyOn(mangasinierFichisteService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ mangasinierFichiste });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(mangasinierFichisteService.update).toHaveBeenCalledWith(mangasinierFichiste);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackComptablePrincipaleById', () => {
        it('Should return tracked ComptablePrincipale primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackComptablePrincipaleById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
