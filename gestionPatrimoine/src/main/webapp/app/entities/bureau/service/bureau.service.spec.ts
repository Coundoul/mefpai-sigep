import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { NomS } from 'app/entities/enumerations/nom-s.model';
import { Direction } from 'app/entities/enumerations/direction.model';
import { IBureau, Bureau } from '../bureau.model';

import { BureauService } from './bureau.service';

describe('Service Tests', () => {
  describe('Bureau Service', () => {
    let service: BureauService;
    let httpMock: HttpTestingController;
    let elemDefault: IBureau;
    let expectedResult: IBureau | IBureau[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(BureauService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nomStructure: NomS.Etablissement,
        direction: Direction.DAGE,
        nomEtablissement: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Bureau', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Bureau()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Bureau', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nomStructure: 'BBBBBB',
            direction: 'BBBBBB',
            nomEtablissement: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Bureau', () => {
        const patchObject = Object.assign({}, new Bureau());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Bureau', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nomStructure: 'BBBBBB',
            direction: 'BBBBBB',
            nomEtablissement: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Bureau', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addBureauToCollectionIfMissing', () => {
        it('should add a Bureau to an empty array', () => {
          const bureau: IBureau = { id: 123 };
          expectedResult = service.addBureauToCollectionIfMissing([], bureau);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(bureau);
        });

        it('should not add a Bureau to an array that contains it', () => {
          const bureau: IBureau = { id: 123 };
          const bureauCollection: IBureau[] = [
            {
              ...bureau,
            },
            { id: 456 },
          ];
          expectedResult = service.addBureauToCollectionIfMissing(bureauCollection, bureau);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Bureau to an array that doesn't contain it", () => {
          const bureau: IBureau = { id: 123 };
          const bureauCollection: IBureau[] = [{ id: 456 }];
          expectedResult = service.addBureauToCollectionIfMissing(bureauCollection, bureau);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(bureau);
        });

        it('should add only unique Bureau to an array', () => {
          const bureauArray: IBureau[] = [{ id: 123 }, { id: 456 }, { id: 9415 }];
          const bureauCollection: IBureau[] = [{ id: 123 }];
          expectedResult = service.addBureauToCollectionIfMissing(bureauCollection, ...bureauArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const bureau: IBureau = { id: 123 };
          const bureau2: IBureau = { id: 456 };
          expectedResult = service.addBureauToCollectionIfMissing([], bureau, bureau2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(bureau);
          expect(expectedResult).toContain(bureau2);
        });

        it('should accept null and undefined values', () => {
          const bureau: IBureau = { id: 123 };
          expectedResult = service.addBureauToCollectionIfMissing([], null, bureau, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(bureau);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
