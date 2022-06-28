import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FicheTechniqueDetailComponent } from './fiche-technique-detail.component';

describe('Component Tests', () => {
  describe('FicheTechnique Management Detail Component', () => {
    let comp: FicheTechniqueDetailComponent;
    let fixture: ComponentFixture<FicheTechniqueDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [FicheTechniqueDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ ficheTechnique: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(FicheTechniqueDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FicheTechniqueDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load ficheTechnique on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.ficheTechnique).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
