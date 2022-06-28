import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ChefEtablissementDetailComponent } from './chef-etablissement-detail.component';

describe('Component Tests', () => {
  describe('ChefEtablissement Management Detail Component', () => {
    let comp: ChefEtablissementDetailComponent;
    let fixture: ComponentFixture<ChefEtablissementDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ChefEtablissementDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ chefEtablissement: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ChefEtablissementDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ChefEtablissementDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load chefEtablissement on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.chefEtablissement).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
