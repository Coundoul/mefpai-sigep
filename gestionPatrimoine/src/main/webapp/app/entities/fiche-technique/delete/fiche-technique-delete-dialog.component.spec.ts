jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { FicheTechniqueService } from '../service/fiche-technique.service';

import { FicheTechniqueDeleteDialogComponent } from './fiche-technique-delete-dialog.component';

describe('Component Tests', () => {
  describe('FicheTechnique Management Delete Component', () => {
    let comp: FicheTechniqueDeleteDialogComponent;
    let fixture: ComponentFixture<FicheTechniqueDeleteDialogComponent>;
    let service: FicheTechniqueService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FicheTechniqueDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(FicheTechniqueDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FicheTechniqueDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(FicheTechniqueService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.close).not.toHaveBeenCalled();
        expect(mockActiveModal.dismiss).toHaveBeenCalled();
      });
    });
  });
});
