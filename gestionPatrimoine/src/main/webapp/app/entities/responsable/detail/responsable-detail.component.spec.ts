import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ResponsableDetailComponent } from './responsable-detail.component';

describe('Component Tests', () => {
  describe('Responsable Management Detail Component', () => {
    let comp: ResponsableDetailComponent;
    let fixture: ComponentFixture<ResponsableDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ResponsableDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ responsable: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ResponsableDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ResponsableDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load responsable on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.responsable).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
