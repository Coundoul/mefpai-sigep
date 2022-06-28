import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ChefProjetDetailComponent } from './chef-projet-detail.component';

describe('Component Tests', () => {
  describe('ChefProjet Management Detail Component', () => {
    let comp: ChefProjetDetailComponent;
    let fixture: ComponentFixture<ChefProjetDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ChefProjetDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ chefProjet: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ChefProjetDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ChefProjetDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load chefProjet on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.chefProjet).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
