jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { BureauService } from '../service/bureau.service';
import { IBureau, Bureau } from '../bureau.model';

import { BureauUpdateComponent } from './bureau-update.component';

describe('Component Tests', () => {
  describe('Bureau Management Update Component', () => {
    let comp: BureauUpdateComponent;
    let fixture: ComponentFixture<BureauUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let bureauService: BureauService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [BureauUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(BureauUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BureauUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      bureauService = TestBed.inject(BureauService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const bureau: IBureau = { id: 456 };

        activatedRoute.data = of({ bureau });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(bureau));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const bureau = { id: 123 };
        spyOn(bureauService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ bureau });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: bureau }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(bureauService.update).toHaveBeenCalledWith(bureau);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const bureau = new Bureau();
        spyOn(bureauService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ bureau });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: bureau }));
        saveSubject.complete();

        // THEN
        expect(bureauService.create).toHaveBeenCalledWith(bureau);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const bureau = { id: 123 };
        spyOn(bureauService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ bureau });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(bureauService.update).toHaveBeenCalledWith(bureau);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
