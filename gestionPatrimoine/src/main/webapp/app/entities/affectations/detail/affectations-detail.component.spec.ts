import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AffectationsDetailComponent } from './affectations-detail.component';

describe('Component Tests', () => {
  describe('Affectations Management Detail Component', () => {
    let comp: AffectationsDetailComponent;
    let fixture: ComponentFixture<AffectationsDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [AffectationsDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ affectations: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(AffectationsDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AffectationsDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load affectations on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.affectations).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
