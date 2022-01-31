import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ProjetAttributionDetailComponent } from './projet-attribution-detail.component';

describe('Component Tests', () => {
  describe('ProjetAttribution Management Detail Component', () => {
    let comp: ProjetAttributionDetailComponent;
    let fixture: ComponentFixture<ProjetAttributionDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ProjetAttributionDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ projetAttribution: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ProjetAttributionDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ProjetAttributionDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load projetAttribution on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.projetAttribution).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
