import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DepartementDetailComponent } from './departement-detail.component';

describe('Component Tests', () => {
  describe('Departement Management Detail Component', () => {
    let comp: DepartementDetailComponent;
    let fixture: ComponentFixture<DepartementDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [DepartementDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ departement: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(DepartementDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DepartementDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load departement on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.departement).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
