import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ComptableSecondaireDetailComponent } from './comptable-secondaire-detail.component';

describe('Component Tests', () => {
  describe('ComptableSecondaire Management Detail Component', () => {
    let comp: ComptableSecondaireDetailComponent;
    let fixture: ComponentFixture<ComptableSecondaireDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ComptableSecondaireDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ comptableSecondaire: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ComptableSecondaireDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ComptableSecondaireDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load comptableSecondaire on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.comptableSecondaire).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
