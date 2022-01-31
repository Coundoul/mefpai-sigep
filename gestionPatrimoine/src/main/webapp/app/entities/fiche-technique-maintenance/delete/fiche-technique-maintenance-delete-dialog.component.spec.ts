jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { FicheTechniqueMaintenanceService } from '../service/fiche-technique-maintenance.service';

import { FicheTechniqueMaintenanceDeleteDialogComponent } from './fiche-technique-maintenance-delete-dialog.component';

describe('Component Tests', () => {
  describe('FicheTechniqueMaintenance Management Delete Component', () => {
    let comp: FicheTechniqueMaintenanceDeleteDialogComponent;
    let fixture: ComponentFixture<FicheTechniqueMaintenanceDeleteDialogComponent>;
    let service: FicheTechniqueMaintenanceService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FicheTechniqueMaintenanceDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(FicheTechniqueMaintenanceDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FicheTechniqueMaintenanceDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(FicheTechniqueMaintenanceService);
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
