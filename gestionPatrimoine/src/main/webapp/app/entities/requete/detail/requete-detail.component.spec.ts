import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RequeteDetailComponent } from './requete-detail.component';

describe('Component Tests', () => {
  describe('Requete Management Detail Component', () => {
    let comp: RequeteDetailComponent;
    let fixture: ComponentFixture<RequeteDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [RequeteDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ requete: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(RequeteDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RequeteDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load requete on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.requete).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
