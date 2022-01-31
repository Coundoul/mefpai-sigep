import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CategorieMatiereDetailComponent } from './categorie-matiere-detail.component';

describe('Component Tests', () => {
  describe('CategorieMatiere Management Detail Component', () => {
    let comp: CategorieMatiereDetailComponent;
    let fixture: ComponentFixture<CategorieMatiereDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CategorieMatiereDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ categorieMatiere: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CategorieMatiereDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CategorieMatiereDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load categorieMatiere on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.categorieMatiere).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
