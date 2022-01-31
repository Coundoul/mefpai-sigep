import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FournisseurDetailComponent } from './fournisseur-detail.component';

describe('Component Tests', () => {
  describe('Fournisseur Management Detail Component', () => {
    let comp: FournisseurDetailComponent;
    let fixture: ComponentFixture<FournisseurDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [FournisseurDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ fournisseur: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(FournisseurDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FournisseurDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load fournisseur on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.fournisseur).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
