import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { INatureFoncier, NatureFoncier } from '../nature-foncier.model';

import { NatureFoncierService } from './nature-foncier.service';

describe('Service Tests', () => {
  describe('NatureFoncier Service', () => {
    let service: NatureFoncierService;
    let httpMock: HttpTestingController;
    let elemDefault: INatureFoncier;
    let expectedResult: INatureFoncier | INatureFoncier[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(NatureFoncierService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        typeFoncier: 'AAAAAAA',
        pieceJointe: 'AAAAAAA',
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

      it('should create a NatureFoncier', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new NatureFoncier()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a NatureFoncier', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            typeFoncier: 'BBBBBB',
            pieceJointe: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a NatureFoncier', () => {
        const patchObject = Object.assign({}, new NatureFoncier());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of NatureFoncier', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            typeFoncier: 'BBBBBB',
            pieceJointe: 'BBBBBB',
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

      it('should delete a NatureFoncier', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addNatureFoncierToCollectionIfMissing', () => {
        it('should add a NatureFoncier to an empty array', () => {
          const natureFoncier: INatureFoncier = { id: 123 };
          expectedResult = service.addNatureFoncierToCollectionIfMissing([], natureFoncier);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(natureFoncier);
        });

        it('should not add a NatureFoncier to an array that contains it', () => {
          const natureFoncier: INatureFoncier = { id: 123 };
          const natureFoncierCollection: INatureFoncier[] = [
            {
              ...natureFoncier,
            },
            { id: 456 },
          ];
          expectedResult = service.addNatureFoncierToCollectionIfMissing(natureFoncierCollection, natureFoncier);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a NatureFoncier to an array that doesn't contain it", () => {
          const natureFoncier: INatureFoncier = { id: 123 };
          const natureFoncierCollection: INatureFoncier[] = [{ id: 456 }];
          expectedResult = service.addNatureFoncierToCollectionIfMissing(natureFoncierCollection, natureFoncier);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(natureFoncier);
        });

        it('should add only unique NatureFoncier to an array', () => {
          const natureFoncierArray: INatureFoncier[] = [{ id: 123 }, { id: 456 }, { id: 15008 }];
          const natureFoncierCollection: INatureFoncier[] = [{ id: 123 }];
          expectedResult = service.addNatureFoncierToCollectionIfMissing(natureFoncierCollection, ...natureFoncierArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const natureFoncier: INatureFoncier = { id: 123 };
          const natureFoncier2: INatureFoncier = { id: 456 };
          expectedResult = service.addNatureFoncierToCollectionIfMissing([], natureFoncier, natureFoncier2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(natureFoncier);
          expect(expectedResult).toContain(natureFoncier2);
        });

        it('should accept null and undefined values', () => {
          const natureFoncier: INatureFoncier = { id: 123 };
          expectedResult = service.addNatureFoncierToCollectionIfMissing([], null, natureFoncier, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(natureFoncier);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
