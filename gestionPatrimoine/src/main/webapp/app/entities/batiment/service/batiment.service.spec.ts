import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBatiment, Batiment } from '../batiment.model';

import { BatimentService } from './batiment.service';

describe('Service Tests', () => {
  describe('Batiment Service', () => {
    let service: BatimentService;
    let httpMock: HttpTestingController;
    let elemDefault: IBatiment;
    let expectedResult: IBatiment | IBatiment[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(BatimentService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nomBatiment: 'AAAAAAA',
        nbrPiece: 'AAAAAAA',
        designation: 'AAAAAAA',
        surface: 0,
        etatGeneral: false,
        description: 'AAAAAAA',
        nombreSalle: 0,
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

      it('should create a Batiment', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Batiment()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Batiment', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nomBatiment: 'BBBBBB',
            nbrPiece: 'BBBBBB',
            designation: 'BBBBBB',
            surface: 1,
            etatGeneral: true,
            description: 'BBBBBB',
            nombreSalle: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Batiment', () => {
        const patchObject = Object.assign(
          {
            nomBatiment: 'BBBBBB',
            surface: 1,
            description: 'BBBBBB',
            nombreSalle: 1,
          },
          new Batiment()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Batiment', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nomBatiment: 'BBBBBB',
            nbrPiece: 'BBBBBB',
            designation: 'BBBBBB',
            surface: 1,
            etatGeneral: true,
            description: 'BBBBBB',
            nombreSalle: 1,
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

      it('should delete a Batiment', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addBatimentToCollectionIfMissing', () => {
        it('should add a Batiment to an empty array', () => {
          const batiment: IBatiment = { id: 123 };
          expectedResult = service.addBatimentToCollectionIfMissing([], batiment);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(batiment);
        });

        it('should not add a Batiment to an array that contains it', () => {
          const batiment: IBatiment = { id: 123 };
          const batimentCollection: IBatiment[] = [
            {
              ...batiment,
            },
            { id: 456 },
          ];
          expectedResult = service.addBatimentToCollectionIfMissing(batimentCollection, batiment);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Batiment to an array that doesn't contain it", () => {
          const batiment: IBatiment = { id: 123 };
          const batimentCollection: IBatiment[] = [{ id: 456 }];
          expectedResult = service.addBatimentToCollectionIfMissing(batimentCollection, batiment);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(batiment);
        });

        it('should add only unique Batiment to an array', () => {
          const batimentArray: IBatiment[] = [{ id: 123 }, { id: 456 }, { id: 13159 }];
          const batimentCollection: IBatiment[] = [{ id: 123 }];
          expectedResult = service.addBatimentToCollectionIfMissing(batimentCollection, ...batimentArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const batiment: IBatiment = { id: 123 };
          const batiment2: IBatiment = { id: 456 };
          expectedResult = service.addBatimentToCollectionIfMissing([], batiment, batiment2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(batiment);
          expect(expectedResult).toContain(batiment2);
        });

        it('should accept null and undefined values', () => {
          const batiment: IBatiment = { id: 123 };
          expectedResult = service.addBatimentToCollectionIfMissing([], null, batiment, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(batiment);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
