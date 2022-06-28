import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MangasinierFichisteDetailComponent } from './mangasinier-fichiste-detail.component';

describe('Component Tests', () => {
  describe('MangasinierFichiste Management Detail Component', () => {
    let comp: MangasinierFichisteDetailComponent;
    let fixture: ComponentFixture<MangasinierFichisteDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [MangasinierFichisteDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ mangasinierFichiste: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(MangasinierFichisteDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MangasinierFichisteDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load mangasinierFichiste on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.mangasinierFichiste).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
