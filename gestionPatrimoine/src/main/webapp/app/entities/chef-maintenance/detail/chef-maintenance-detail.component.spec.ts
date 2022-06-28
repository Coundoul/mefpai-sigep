import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ChefMaintenanceDetailComponent } from './chef-maintenance-detail.component';

describe('Component Tests', () => {
  describe('ChefMaintenance Management Detail Component', () => {
    let comp: ChefMaintenanceDetailComponent;
    let fixture: ComponentFixture<ChefMaintenanceDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ChefMaintenanceDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ chefMaintenance: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ChefMaintenanceDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ChefMaintenanceDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load chefMaintenance on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.chefMaintenance).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
