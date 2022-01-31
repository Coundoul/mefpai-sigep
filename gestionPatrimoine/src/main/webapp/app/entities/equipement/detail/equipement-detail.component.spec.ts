import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DataUtils } from 'app/core/util/data-util.service';

import { EquipementDetailComponent } from './equipement-detail.component';

describe('Component Tests', () => {
  describe('Equipement Management Detail Component', () => {
    let comp: EquipementDetailComponent;
    let fixture: ComponentFixture<EquipementDetailComponent>;
    let dataUtils: DataUtils;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [EquipementDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ equipement: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(EquipementDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EquipementDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = TestBed.inject(DataUtils);
    });

    describe('OnInit', () => {
      it('Should load equipement on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.equipement).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });

    describe('byteSize', () => {
      it('Should call byteSize from DataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'byteSize');
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.byteSize(fakeBase64);

        // THEN
        expect(dataUtils.byteSize).toBeCalledWith(fakeBase64);
      });
    });

    describe('openFile', () => {
      it('Should call openFile from DataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'openFile');
        const fakeContentType = 'fake content type';
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.openFile(fakeBase64, fakeContentType);

        // THEN
        expect(dataUtils.openFile).toBeCalledWith(fakeBase64, fakeContentType);
      });
    });
  });
});
