import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BatimentDetailComponent } from './batiment-detail.component';

describe('Component Tests', () => {
  describe('Batiment Management Detail Component', () => {
    let comp: BatimentDetailComponent;
    let fixture: ComponentFixture<BatimentDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [BatimentDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ batiment: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(BatimentDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BatimentDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load batiment on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.batiment).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
