import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EtapesDetailComponent } from './etapes-detail.component';

describe('Component Tests', () => {
  describe('Etapes Management Detail Component', () => {
    let comp: EtapesDetailComponent;
    let fixture: ComponentFixture<EtapesDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [EtapesDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ etapes: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(EtapesDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EtapesDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load etapes on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.etapes).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
