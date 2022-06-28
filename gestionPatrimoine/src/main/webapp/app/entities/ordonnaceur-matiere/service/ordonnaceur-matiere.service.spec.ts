import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Sexe } from 'app/entities/enumerations/sexe.model';
import { Direction } from 'app/entities/enumerations/direction.model';
import { IOrdonnaceurMatiere, OrdonnaceurMatiere } from '../ordonnaceur-matiere.model';

import { OrdonnaceurMatiereService } from './ordonnaceur-matiere.service';

describe('Service Tests', () => {
  describe('OrdonnaceurMatiere Service', () => {
    let service: OrdonnaceurMatiereService;
    let httpMock: HttpTestingController;
    let elemDefault: IOrdonnaceurMatiere;
    let expectedResult: IOrdonnaceurMatiere | IOrdonnaceurMatiere[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(OrdonnaceurMatiereService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nomPers: 'AAAAAAA',
        prenomPers: 'AAAAAAA',
        sexe: Sexe.Masculin,
        mobile: 'AAAAAAA',
        adresse: 'AAAAAAA',
        direction: Direction.DAGE,
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

      it('should create a OrdonnaceurMatiere', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new OrdonnaceurMatiere()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a OrdonnaceurMatiere', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nomPers: 'BBBBBB',
            prenomPers: 'BBBBBB',
            sexe: 'BBBBBB',
            mobile: 'BBBBBB',
            adresse: 'BBBBBB',
            direction: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a OrdonnaceurMatiere', () => {
        const patchObject = Object.assign(
          {
            mobile: 'BBBBBB',
            adresse: 'BBBBBB',
            direction: 'BBBBBB',
          },
          new OrdonnaceurMatiere()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of OrdonnaceurMatiere', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nomPers: 'BBBBBB',
            prenomPers: 'BBBBBB',
            sexe: 'BBBBBB',
            mobile: 'BBBBBB',
            adresse: 'BBBBBB',
            direction: 'BBBBBB',
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

      it('should delete a OrdonnaceurMatiere', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addOrdonnaceurMatiereToCollectionIfMissing', () => {
        it('should add a OrdonnaceurMatiere to an empty array', () => {
          const ordonnaceurMatiere: IOrdonnaceurMatiere = { id: 123 };
          expectedResult = service.addOrdonnaceurMatiereToCollectionIfMissing([], ordonnaceurMatiere);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(ordonnaceurMatiere);
        });

        it('should not add a OrdonnaceurMatiere to an array that contains it', () => {
          const ordonnaceurMatiere: IOrdonnaceurMatiere = { id: 123 };
          const ordonnaceurMatiereCollection: IOrdonnaceurMatiere[] = [
            {
              ...ordonnaceurMatiere,
            },
            { id: 456 },
          ];
          expectedResult = service.addOrdonnaceurMatiereToCollectionIfMissing(ordonnaceurMatiereCollection, ordonnaceurMatiere);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a OrdonnaceurMatiere to an array that doesn't contain it", () => {
          const ordonnaceurMatiere: IOrdonnaceurMatiere = { id: 123 };
          const ordonnaceurMatiereCollection: IOrdonnaceurMatiere[] = [{ id: 456 }];
          expectedResult = service.addOrdonnaceurMatiereToCollectionIfMissing(ordonnaceurMatiereCollection, ordonnaceurMatiere);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(ordonnaceurMatiere);
        });

        it('should add only unique OrdonnaceurMatiere to an array', () => {
          const ordonnaceurMatiereArray: IOrdonnaceurMatiere[] = [{ id: 123 }, { id: 456 }, { id: 12936 }];
          const ordonnaceurMatiereCollection: IOrdonnaceurMatiere[] = [{ id: 123 }];
          expectedResult = service.addOrdonnaceurMatiereToCollectionIfMissing(ordonnaceurMatiereCollection, ...ordonnaceurMatiereArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const ordonnaceurMatiere: IOrdonnaceurMatiere = { id: 123 };
          const ordonnaceurMatiere2: IOrdonnaceurMatiere = { id: 456 };
          expectedResult = service.addOrdonnaceurMatiereToCollectionIfMissing([], ordonnaceurMatiere, ordonnaceurMatiere2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(ordonnaceurMatiere);
          expect(expectedResult).toContain(ordonnaceurMatiere2);
        });

        it('should accept null and undefined values', () => {
          const ordonnaceurMatiere: IOrdonnaceurMatiere = { id: 123 };
          expectedResult = service.addOrdonnaceurMatiereToCollectionIfMissing([], null, ordonnaceurMatiere, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(ordonnaceurMatiere);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
