import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ComptablePrincipaleDetailComponent } from './comptable-principale-detail.component';

describe('Component Tests', () => {
  describe('ComptablePrincipale Management Detail Component', () => {
    let comp: ComptablePrincipaleDetailComponent;
    let fixture: ComponentFixture<ComptablePrincipaleDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ComptablePrincipaleDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ comptablePrincipale: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ComptablePrincipaleDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ComptablePrincipaleDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load comptablePrincipale on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.comptablePrincipale).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
