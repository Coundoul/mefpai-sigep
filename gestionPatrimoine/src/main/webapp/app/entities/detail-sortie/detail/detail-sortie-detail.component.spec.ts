import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DetailSortieDetailComponent } from './detail-sortie-detail.component';

describe('Component Tests', () => {
  describe('DetailSortie Management Detail Component', () => {
    let comp: DetailSortieDetailComponent;
    let fixture: ComponentFixture<DetailSortieDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [DetailSortieDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ detailSortie: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(DetailSortieDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DetailSortieDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load detailSortie on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.detailSortie).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
