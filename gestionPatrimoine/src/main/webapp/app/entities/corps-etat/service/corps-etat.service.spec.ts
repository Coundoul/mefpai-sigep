import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICorpsEtat, CorpsEtat } from '../corps-etat.model';

import { CorpsEtatService } from './corps-etat.service';

describe('Service Tests', () => {
  describe('CorpsEtat Service', () => {
    let service: CorpsEtatService;
    let httpMock: HttpTestingController;
    let elemDefault: ICorpsEtat;
    let expectedResult: ICorpsEtat | ICorpsEtat[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CorpsEtatService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nomCorps: 'AAAAAAA',
        grosOeuvre: 'AAAAAAA',
        descriptionGrosOeuvre: 'AAAAAAA',
        secondOeuvre: 'AAAAAAA',
        descriptionSecondOeuvre: 'AAAAAAA',
        oservation: false,
        etatCorps: false,
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

      it('should create a CorpsEtat', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new CorpsEtat()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a CorpsEtat', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nomCorps: 'BBBBBB',
            grosOeuvre: 'BBBBBB',
            descriptionGrosOeuvre: 'BBBBBB',
            secondOeuvre: 'BBBBBB',
            descriptionSecondOeuvre: 'BBBBBB',
            oservation: true,
            etatCorps: true,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a CorpsEtat', () => {
        const patchObject = Object.assign(
          {
            nomCorps: 'BBBBBB',
            oservation: true,
            etatCorps: true,
          },
          new CorpsEtat()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of CorpsEtat', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nomCorps: 'BBBBBB',
            grosOeuvre: 'BBBBBB',
            descriptionGrosOeuvre: 'BBBBBB',
            secondOeuvre: 'BBBBBB',
            descriptionSecondOeuvre: 'BBBBBB',
            oservation: true,
            etatCorps: true,
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

      it('should delete a CorpsEtat', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCorpsEtatToCollectionIfMissing', () => {
        it('should add a CorpsEtat to an empty array', () => {
          const corpsEtat: ICorpsEtat = { id: 123 };
          expectedResult = service.addCorpsEtatToCollectionIfMissing([], corpsEtat);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(corpsEtat);
        });

        it('should not add a CorpsEtat to an array that contains it', () => {
          const corpsEtat: ICorpsEtat = { id: 123 };
          const corpsEtatCollection: ICorpsEtat[] = [
            {
              ...corpsEtat,
            },
            { id: 456 },
          ];
          expectedResult = service.addCorpsEtatToCollectionIfMissing(corpsEtatCollection, corpsEtat);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a CorpsEtat to an array that doesn't contain it", () => {
          const corpsEtat: ICorpsEtat = { id: 123 };
          const corpsEtatCollection: ICorpsEtat[] = [{ id: 456 }];
          expectedResult = service.addCorpsEtatToCollectionIfMissing(corpsEtatCollection, corpsEtat);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(corpsEtat);
        });

        it('should add only unique CorpsEtat to an array', () => {
          const corpsEtatArray: ICorpsEtat[] = [{ id: 123 }, { id: 456 }, { id: 32476 }];
          const corpsEtatCollection: ICorpsEtat[] = [{ id: 123 }];
          expectedResult = service.addCorpsEtatToCollectionIfMissing(corpsEtatCollection, ...corpsEtatArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const corpsEtat: ICorpsEtat = { id: 123 };
          const corpsEtat2: ICorpsEtat = { id: 456 };
          expectedResult = service.addCorpsEtatToCollectionIfMissing([], corpsEtat, corpsEtat2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(corpsEtat);
          expect(expectedResult).toContain(corpsEtat2);
        });

        it('should accept null and undefined values', () => {
          const corpsEtat: ICorpsEtat = { id: 123 };
          expectedResult = service.addCorpsEtatToCollectionIfMissing([], null, corpsEtat, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(corpsEtat);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
