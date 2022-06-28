import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OrdonnaceurMatiereDetailComponent } from './ordonnaceur-matiere-detail.component';

describe('Component Tests', () => {
  describe('OrdonnaceurMatiere Management Detail Component', () => {
    let comp: OrdonnaceurMatiereDetailComponent;
    let fixture: ComponentFixture<OrdonnaceurMatiereDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [OrdonnaceurMatiereDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ ordonnaceurMatiere: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(OrdonnaceurMatiereDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(OrdonnaceurMatiereDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load ordonnaceurMatiere on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.ordonnaceurMatiere).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
