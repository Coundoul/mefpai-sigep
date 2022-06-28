import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SallesDetailComponent } from './salles-detail.component';

describe('Component Tests', () => {
  describe('Salles Management Detail Component', () => {
    let comp: SallesDetailComponent;
    let fixture: ComponentFixture<SallesDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [SallesDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ salles: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(SallesDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SallesDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load salles on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.salles).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
