jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ChefProjetService } from '../service/chef-projet.service';

import { ChefProjetDeleteDialogComponent } from './chef-projet-delete-dialog.component';

describe('Component Tests', () => {
  describe('ChefProjet Management Delete Component', () => {
    let comp: ChefProjetDeleteDialogComponent;
    let fixture: ComponentFixture<ChefProjetDeleteDialogComponent>;
    let service: ChefProjetService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ChefProjetDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(ChefProjetDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ChefProjetDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ChefProjetService);
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
