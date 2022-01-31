import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IntendantDetailComponent } from './intendant-detail.component';

describe('Component Tests', () => {
  describe('Intendant Management Detail Component', () => {
    let comp: IntendantDetailComponent;
    let fixture: ComponentFixture<IntendantDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [IntendantDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ intendant: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(IntendantDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(IntendantDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load intendant on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.intendant).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
