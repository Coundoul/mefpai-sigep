jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { DetailSortieService } from '../service/detail-sortie.service';

import { DetailSortieDeleteDialogComponent } from './detail-sortie-delete-dialog.component';

describe('Component Tests', () => {
  describe('DetailSortie Management Delete Component', () => {
    let comp: DetailSortieDeleteDialogComponent;
    let fixture: ComponentFixture<DetailSortieDeleteDialogComponent>;
    let service: DetailSortieService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DetailSortieDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(DetailSortieDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DetailSortieDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(DetailSortieService);
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
