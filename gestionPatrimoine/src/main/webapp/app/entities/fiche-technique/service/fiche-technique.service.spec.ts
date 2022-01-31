import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFicheTechnique, FicheTechnique } from '../fiche-technique.model';

import { FicheTechniqueService } from './fiche-technique.service';

describe('Service Tests', () => {
  describe('FicheTechnique Service', () => {
    let service: FicheTechniqueService;
    let httpMock: HttpTestingController;
    let elemDefault: IFicheTechnique;
    let expectedResult: IFicheTechnique | IFicheTechnique[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(FicheTechniqueService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        pieceJointe: 'AAAAAAA',
        dateDepot: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateDepot: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a FicheTechnique', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateDepot: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateDepot: currentDate,
          },
          returnedFromService
        );

        service.create(new FicheTechnique()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a FicheTechnique', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            pieceJointe: 'BBBBBB',
            dateDepot: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateDepot: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a FicheTechnique', () => {
        const patchObject = Object.assign(
          {
            pieceJointe: 'BBBBBB',
            dateDepot: currentDate.format(DATE_TIME_FORMAT),
          },
          new FicheTechnique()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            dateDepot: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of FicheTechnique', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            pieceJointe: 'BBBBBB',
            dateDepot: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateDepot: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a FicheTechnique', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addFicheTechniqueToCollectionIfMissing', () => {
        it('should add a FicheTechnique to an empty array', () => {
          const ficheTechnique: IFicheTechnique = { id: 123 };
          expectedResult = service.addFicheTechniqueToCollectionIfMissing([], ficheTechnique);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(ficheTechnique);
        });

        it('should not add a FicheTechnique to an array that contains it', () => {
          const ficheTechnique: IFicheTechnique = { id: 123 };
          const ficheTechniqueCollection: IFicheTechnique[] = [
            {
              ...ficheTechnique,
            },
            { id: 456 },
          ];
          expectedResult = service.addFicheTechniqueToCollectionIfMissing(ficheTechniqueCollection, ficheTechnique);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a FicheTechnique to an array that doesn't contain it", () => {
          const ficheTechnique: IFicheTechnique = { id: 123 };
          const ficheTechniqueCollection: IFicheTechnique[] = [{ id: 456 }];
          expectedResult = service.addFicheTechniqueToCollectionIfMissing(ficheTechniqueCollection, ficheTechnique);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(ficheTechnique);
        });

        it('should add only unique FicheTechnique to an array', () => {
          const ficheTechniqueArray: IFicheTechnique[] = [{ id: 123 }, { id: 456 }, { id: 34679 }];
          const ficheTechniqueCollection: IFicheTechnique[] = [{ id: 123 }];
          expectedResult = service.addFicheTechniqueToCollectionIfMissing(ficheTechniqueCollection, ...ficheTechniqueArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const ficheTechnique: IFicheTechnique = { id: 123 };
          const ficheTechnique2: IFicheTechnique = { id: 456 };
          expectedResult = service.addFicheTechniqueToCollectionIfMissing([], ficheTechnique, ficheTechnique2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(ficheTechnique);
          expect(expectedResult).toContain(ficheTechnique2);
        });

        it('should accept null and undefined values', () => {
          const ficheTechnique: IFicheTechnique = { id: 123 };
          expectedResult = service.addFicheTechniqueToCollectionIfMissing([], null, ficheTechnique, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(ficheTechnique);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
