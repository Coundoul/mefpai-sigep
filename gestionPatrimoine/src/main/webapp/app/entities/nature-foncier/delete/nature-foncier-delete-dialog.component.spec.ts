jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { NatureFoncierService } from '../service/nature-foncier.service';

import { NatureFoncierDeleteDialogComponent } from './nature-foncier-delete-dialog.component';

describe('Component Tests', () => {
  describe('NatureFoncier Management Delete Component', () => {
    let comp: NatureFoncierDeleteDialogComponent;
    let fixture: ComponentFixture<NatureFoncierDeleteDialogComponent>;
    let service: NatureFoncierService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [NatureFoncierDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(NatureFoncierDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(NatureFoncierDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(NatureFoncierService);
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
