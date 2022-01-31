import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ContratProjetDetailComponent } from './contrat-projet-detail.component';

describe('Component Tests', () => {
  describe('ContratProjet Management Detail Component', () => {
    let comp: ContratProjetDetailComponent;
    let fixture: ComponentFixture<ContratProjetDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ContratProjetDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ contratProjet: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ContratProjetDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ContratProjetDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load contratProjet on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.contratProjet).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
