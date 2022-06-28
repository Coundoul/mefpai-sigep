import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AttributionInfrastructureDetailComponent } from './attribution-infrastructure-detail.component';

describe('Component Tests', () => {
  describe('AttributionInfrastructure Management Detail Component', () => {
    let comp: AttributionInfrastructureDetailComponent;
    let fixture: ComponentFixture<AttributionInfrastructureDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [AttributionInfrastructureDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ attributionInfrastructure: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(AttributionInfrastructureDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AttributionInfrastructureDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load attributionInfrastructure on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.attributionInfrastructure).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
