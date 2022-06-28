import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITypeBatiment, TypeBatiment } from '../type-batiment.model';

import { TypeBatimentService } from './type-batiment.service';

describe('Service Tests', () => {
  describe('TypeBatiment Service', () => {
    let service: TypeBatimentService;
    let httpMock: HttpTestingController;
    let elemDefault: ITypeBatiment;
    let expectedResult: ITypeBatiment | ITypeBatiment[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(TypeBatimentService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        typeBa: 'AAAAAAA',
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

      it('should create a TypeBatiment', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new TypeBatiment()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a TypeBatiment', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            typeBa: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a TypeBatiment', () => {
        const patchObject = Object.assign(
          {
            typeBa: 'BBBBBB',
          },
          new TypeBatiment()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of TypeBatiment', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            typeBa: 'BBBBBB',
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

      it('should delete a TypeBatiment', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addTypeBatimentToCollectionIfMissing', () => {
        it('should add a TypeBatiment to an empty array', () => {
          const typeBatiment: ITypeBatiment = { id: 123 };
          expectedResult = service.addTypeBatimentToCollectionIfMissing([], typeBatiment);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(typeBatiment);
        });

        it('should not add a TypeBatiment to an array that contains it', () => {
          const typeBatiment: ITypeBatiment = { id: 123 };
          const typeBatimentCollection: ITypeBatiment[] = [
            {
              ...typeBatiment,
            },
            { id: 456 },
          ];
          expectedResult = service.addTypeBatimentToCollectionIfMissing(typeBatimentCollection, typeBatiment);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a TypeBatiment to an array that doesn't contain it", () => {
          const typeBatiment: ITypeBatiment = { id: 123 };
          const typeBatimentCollection: ITypeBatiment[] = [{ id: 456 }];
          expectedResult = service.addTypeBatimentToCollectionIfMissing(typeBatimentCollection, typeBatiment);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(typeBatiment);
        });

        it('should add only unique TypeBatiment to an array', () => {
          const typeBatimentArray: ITypeBatiment[] = [{ id: 123 }, { id: 456 }, { id: 55578 }];
          const typeBatimentCollection: ITypeBatiment[] = [{ id: 123 }];
          expectedResult = service.addTypeBatimentToCollectionIfMissing(typeBatimentCollection, ...typeBatimentArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const typeBatiment: ITypeBatiment = { id: 123 };
          const typeBatiment2: ITypeBatiment = { id: 456 };
          expectedResult = service.addTypeBatimentToCollectionIfMissing([], typeBatiment, typeBatiment2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(typeBatiment);
          expect(expectedResult).toContain(typeBatiment2);
        });

        it('should accept null and undefined values', () => {
          const typeBatiment: ITypeBatiment = { id: 123 };
          expectedResult = service.addTypeBatimentToCollectionIfMissing([], null, typeBatiment, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(typeBatiment);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
