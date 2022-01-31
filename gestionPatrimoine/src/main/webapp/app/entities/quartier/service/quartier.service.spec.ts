import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IQuartier, Quartier } from '../quartier.model';

import { QuartierService } from './quartier.service';

describe('Service Tests', () => {
  describe('Quartier Service', () => {
    let service: QuartierService;
    let httpMock: HttpTestingController;
    let elemDefault: IQuartier;
    let expectedResult: IQuartier | IQuartier[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(QuartierService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nomQuartier: 'AAAAAAA',
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

      it('should create a Quartier', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Quartier()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Quartier', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nomQuartier: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Quartier', () => {
        const patchObject = Object.assign(
          {
            nomQuartier: 'BBBBBB',
          },
          new Quartier()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Quartier', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nomQuartier: 'BBBBBB',
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

      it('should delete a Quartier', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addQuartierToCollectionIfMissing', () => {
        it('should add a Quartier to an empty array', () => {
          const quartier: IQuartier = { id: 123 };
          expectedResult = service.addQuartierToCollectionIfMissing([], quartier);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(quartier);
        });

        it('should not add a Quartier to an array that contains it', () => {
          const quartier: IQuartier = { id: 123 };
          const quartierCollection: IQuartier[] = [
            {
              ...quartier,
            },
            { id: 456 },
          ];
          expectedResult = service.addQuartierToCollectionIfMissing(quartierCollection, quartier);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Quartier to an array that doesn't contain it", () => {
          const quartier: IQuartier = { id: 123 };
          const quartierCollection: IQuartier[] = [{ id: 456 }];
          expectedResult = service.addQuartierToCollectionIfMissing(quartierCollection, quartier);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(quartier);
        });

        it('should add only unique Quartier to an array', () => {
          const quartierArray: IQuartier[] = [{ id: 123 }, { id: 456 }, { id: 30403 }];
          const quartierCollection: IQuartier[] = [{ id: 123 }];
          expectedResult = service.addQuartierToCollectionIfMissing(quartierCollection, ...quartierArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const quartier: IQuartier = { id: 123 };
          const quartier2: IQuartier = { id: 456 };
          expectedResult = service.addQuartierToCollectionIfMissing([], quartier, quartier2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(quartier);
          expect(expectedResult).toContain(quartier2);
        });

        it('should accept null and undefined values', () => {
          const quartier: IQuartier = { id: 123 };
          expectedResult = service.addQuartierToCollectionIfMissing([], null, quartier, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(quartier);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
