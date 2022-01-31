import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { NatureFoncierDetailComponent } from './nature-foncier-detail.component';

describe('Component Tests', () => {
  describe('NatureFoncier Management Detail Component', () => {
    let comp: NatureFoncierDetailComponent;
    let fixture: ComponentFixture<NatureFoncierDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [NatureFoncierDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ natureFoncier: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(NatureFoncierDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(NatureFoncierDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load natureFoncier on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.natureFoncier).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
