import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DetenteurDetailComponent } from './detenteur-detail.component';

describe('Component Tests', () => {
  describe('Detenteur Management Detail Component', () => {
    let comp: DetenteurDetailComponent;
    let fixture: ComponentFixture<DetenteurDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [DetenteurDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ detenteur: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(DetenteurDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DetenteurDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load detenteur on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.detenteur).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
