import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IntervenantDetailComponent } from './intervenant-detail.component';

describe('Component Tests', () => {
  describe('Intervenant Management Detail Component', () => {
    let comp: IntervenantDetailComponent;
    let fixture: ComponentFixture<IntervenantDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [IntervenantDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ intervenant: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(IntervenantDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(IntervenantDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load intervenant on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.intervenant).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
