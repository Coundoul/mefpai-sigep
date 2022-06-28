import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { UtilisateurFinalDetailComponent } from './utilisateur-final-detail.component';

describe('Component Tests', () => {
  describe('UtilisateurFinal Management Detail Component', () => {
    let comp: UtilisateurFinalDetailComponent;
    let fixture: ComponentFixture<UtilisateurFinalDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [UtilisateurFinalDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ utilisateurFinal: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(UtilisateurFinalDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(UtilisateurFinalDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load utilisateurFinal on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.utilisateurFinal).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
