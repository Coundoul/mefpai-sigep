import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FormateursDetailComponent } from './formateurs-detail.component';

describe('Component Tests', () => {
  describe('Formateurs Management Detail Component', () => {
    let comp: FormateursDetailComponent;
    let fixture: ComponentFixture<FormateursDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [FormateursDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ formateurs: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(FormateursDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FormateursDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load formateurs on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.formateurs).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
