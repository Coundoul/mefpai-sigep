import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FicheTechniqueMaintenanceDetailComponent } from './fiche-technique-maintenance-detail.component';

describe('Component Tests', () => {
  describe('FicheTechniqueMaintenance Management Detail Component', () => {
    let comp: FicheTechniqueMaintenanceDetailComponent;
    let fixture: ComponentFixture<FicheTechniqueMaintenanceDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [FicheTechniqueMaintenanceDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ ficheTechniqueMaintenance: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(FicheTechniqueMaintenanceDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FicheTechniqueMaintenanceDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load ficheTechniqueMaintenance on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.ficheTechniqueMaintenance).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
