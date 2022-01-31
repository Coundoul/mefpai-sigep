import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Sexe } from 'app/entities/enumerations/sexe.model';
import { IIntendant, Intendant } from '../intendant.model';

import { IntendantService } from './intendant.service';

describe('Service Tests', () => {
  describe('Intendant Service', () => {
    let service: IntendantService;
    let httpMock: HttpTestingController;
    let elemDefault: IIntendant;
    let expectedResult: IIntendant | IIntendant[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(IntendantService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nomPers: 'AAAAAAA',
        prenomPers: 'AAAAAAA',
        sexe: Sexe.Masculin,
        mobile: 'AAAAAAA',
        adresse: 'AAAAAAA',
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

      it('should create a Intendant', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Intendant()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Intendant', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nomPers: 'BBBBBB',
            prenomPers: 'BBBBBB',
            sexe: 'BBBBBB',
            mobile: 'BBBBBB',
            adresse: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Intendant', () => {
        const patchObject = Object.assign(
          {
            prenomPers: 'BBBBBB',
            sexe: 'BBBBBB',
            mobile: 'BBBBBB',
          },
          new Intendant()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Intendant', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nomPers: 'BBBBBB',
            prenomPers: 'BBBBBB',
            sexe: 'BBBBBB',
            mobile: 'BBBBBB',
            adresse: 'BBBBBB',
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

      it('should delete a Intendant', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addIntendantToCollectionIfMissing', () => {
        it('should add a Intendant to an empty array', () => {
          const intendant: IIntendant = { id: 123 };
          expectedResult = service.addIntendantToCollectionIfMissing([], intendant);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(intendant);
        });

        it('should not add a Intendant to an array that contains it', () => {
          const intendant: IIntendant = { id: 123 };
          const intendantCollection: IIntendant[] = [
            {
              ...intendant,
            },
            { id: 456 },
          ];
          expectedResult = service.addIntendantToCollectionIfMissing(intendantCollection, intendant);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Intendant to an array that doesn't contain it", () => {
          const intendant: IIntendant = { id: 123 };
          const intendantCollection: IIntendant[] = [{ id: 456 }];
          expectedResult = service.addIntendantToCollectionIfMissing(intendantCollection, intendant);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(intendant);
        });

        it('should add only unique Intendant to an array', () => {
          const intendantArray: IIntendant[] = [{ id: 123 }, { id: 456 }, { id: 5652 }];
          const intendantCollection: IIntendant[] = [{ id: 123 }];
          expectedResult = service.addIntendantToCollectionIfMissing(intendantCollection, ...intendantArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const intendant: IIntendant = { id: 123 };
          const intendant2: IIntendant = { id: 456 };
          expectedResult = service.addIntendantToCollectionIfMissing([], intendant, intendant2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(intendant);
          expect(expectedResult).toContain(intendant2);
        });

        it('should accept null and undefined values', () => {
          const intendant: IIntendant = { id: 123 };
          expectedResult = service.addIntendantToCollectionIfMissing([], null, intendant, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(intendant);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
