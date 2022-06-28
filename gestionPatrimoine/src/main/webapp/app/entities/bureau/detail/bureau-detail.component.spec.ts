import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BureauDetailComponent } from './bureau-detail.component';

describe('Component Tests', () => {
  describe('Bureau Management Detail Component', () => {
    let comp: BureauDetailComponent;
    let fixture: ComponentFixture<BureauDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [BureauDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ bureau: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(BureauDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BureauDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load bureau on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.bureau).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
