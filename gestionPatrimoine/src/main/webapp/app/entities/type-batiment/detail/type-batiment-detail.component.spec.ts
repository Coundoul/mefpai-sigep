import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TypeBatimentDetailComponent } from './type-batiment-detail.component';

describe('Component Tests', () => {
  describe('TypeBatiment Management Detail Component', () => {
    let comp: TypeBatimentDetailComponent;
    let fixture: ComponentFixture<TypeBatimentDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [TypeBatimentDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ typeBatiment: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(TypeBatimentDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TypeBatimentDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load typeBatiment on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.typeBatiment).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
