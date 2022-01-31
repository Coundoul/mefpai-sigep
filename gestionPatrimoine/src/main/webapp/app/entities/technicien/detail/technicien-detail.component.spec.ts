import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TechnicienDetailComponent } from './technicien-detail.component';

describe('Component Tests', () => {
  describe('Technicien Management Detail Component', () => {
    let comp: TechnicienDetailComponent;
    let fixture: ComponentFixture<TechnicienDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [TechnicienDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ technicien: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(TechnicienDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TechnicienDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load technicien on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.technicien).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
