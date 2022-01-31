import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ProjetsDetailComponent } from './projets-detail.component';

describe('Component Tests', () => {
  describe('Projets Management Detail Component', () => {
    let comp: ProjetsDetailComponent;
    let fixture: ComponentFixture<ProjetsDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ProjetsDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ projets: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ProjetsDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ProjetsDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load projets on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.projets).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
