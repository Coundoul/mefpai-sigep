import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CorpsEtatDetailComponent } from './corps-etat-detail.component';

describe('Component Tests', () => {
  describe('CorpsEtat Management Detail Component', () => {
    let comp: CorpsEtatDetailComponent;
    let fixture: ComponentFixture<CorpsEtatDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CorpsEtatDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ corpsEtat: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CorpsEtatDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CorpsEtatDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load corpsEtat on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.corpsEtat).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
