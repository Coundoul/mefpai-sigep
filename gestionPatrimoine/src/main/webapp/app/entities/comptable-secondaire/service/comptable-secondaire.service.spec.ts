import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Sexe } from 'app/entities/enumerations/sexe.model';
import { Direction } from 'app/entities/enumerations/direction.model';
import { IComptableSecondaire, ComptableSecondaire } from '../comptable-secondaire.model';

import { ComptableSecondaireService } from './comptable-secondaire.service';

describe('Service Tests', () => {
  describe('ComptableSecondaire Service', () => {
    let service: ComptableSecondaireService;
    let httpMock: HttpTestingController;
    let elemDefault: IComptableSecondaire;
    let expectedResult: IComptableSecondaire | IComptableSecondaire[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ComptableSecondaireService);
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

      it('should create a ComptableSecondaire', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ComptableSecondaire()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ComptableSecondaire', () => {
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

      it('should partial update a ComptableSecondaire', () => {
        const patchObject = Object.assign(
          {
            prenomPers: 'BBBBBB',
          },
          new ComptableSecondaire()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ComptableSecondaire', () => {
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

      it('should delete a ComptableSecondaire', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addComptableSecondaireToCollectionIfMissing', () => {
        it('should add a ComptableSecondaire to an empty array', () => {
          const comptableSecondaire: IComptableSecondaire = { id: 123 };
          expectedResult = service.addComptableSecondaireToCollectionIfMissing([], comptableSecondaire);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(comptableSecondaire);
        });

        it('should not add a ComptableSecondaire to an array that contains it', () => {
          const comptableSecondaire: IComptableSecondaire = { id: 123 };
          const comptableSecondaireCollection: IComptableSecondaire[] = [
            {
              ...comptableSecondaire,
            },
            { id: 456 },
          ];
          expectedResult = service.addComptableSecondaireToCollectionIfMissing(comptableSecondaireCollection, comptableSecondaire);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ComptableSecondaire to an array that doesn't contain it", () => {
          const comptableSecondaire: IComptableSecondaire = { id: 123 };
          const comptableSecondaireCollection: IComptableSecondaire[] = [{ id: 456 }];
          expectedResult = service.addComptableSecondaireToCollectionIfMissing(comptableSecondaireCollection, comptableSecondaire);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(comptableSecondaire);
        });

        it('should add only unique ComptableSecondaire to an array', () => {
          const comptableSecondaireArray: IComptableSecondaire[] = [{ id: 123 }, { id: 456 }, { id: 2089 }];
          const comptableSecondaireCollection: IComptableSecondaire[] = [{ id: 123 }];
          expectedResult = service.addComptableSecondaireToCollectionIfMissing(comptableSecondaireCollection, ...comptableSecondaireArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const comptableSecondaire: IComptableSecondaire = { id: 123 };
          const comptableSecondaire2: IComptableSecondaire = { id: 456 };
          expectedResult = service.addComptableSecondaireToCollectionIfMissing([], comptableSecondaire, comptableSecondaire2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(comptableSecondaire);
          expect(expectedResult).toContain(comptableSecondaire2);
        });

        it('should accept null and undefined values', () => {
          const comptableSecondaire: IComptableSecondaire = { id: 123 };
          expectedResult = service.addComptableSecondaireToCollectionIfMissing([], null, comptableSecondaire, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(comptableSecondaire);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
