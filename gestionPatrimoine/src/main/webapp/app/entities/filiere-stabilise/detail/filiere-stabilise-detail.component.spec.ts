import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FiliereStabiliseDetailComponent } from './filiere-stabilise-detail.component';

describe('Component Tests', () => {
  describe('FiliereStabilise Management Detail Component', () => {
    let comp: FiliereStabiliseDetailComponent;
    let fixture: ComponentFixture<FiliereStabiliseDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [FiliereStabiliseDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ filiereStabilise: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(FiliereStabiliseDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FiliereStabiliseDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load filiereStabilise on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.filiereStabilise).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
