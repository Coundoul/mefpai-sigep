import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Sexe } from 'app/entities/enumerations/sexe.model';
import { Direction } from 'app/entities/enumerations/direction.model';
import { IComptablePrincipale, ComptablePrincipale } from '../comptable-principale.model';

import { ComptablePrincipaleService } from './comptable-principale.service';

describe('Service Tests', () => {
  describe('ComptablePrincipale Service', () => {
    let service: ComptablePrincipaleService;
    let httpMock: HttpTestingController;
    let elemDefault: IComptablePrincipale;
    let expectedResult: IComptablePrincipale | IComptablePrincipale[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ComptablePrincipaleService);
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

      it('should create a ComptablePrincipale', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ComptablePrincipale()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ComptablePrincipale', () => {
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

      it('should partial update a ComptablePrincipale', () => {
        const patchObject = Object.assign(
          {
            direction: 'BBBBBB',
          },
          new ComptablePrincipale()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ComptablePrincipale', () => {
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

      it('should delete a ComptablePrincipale', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addComptablePrincipaleToCollectionIfMissing', () => {
        it('should add a ComptablePrincipale to an empty array', () => {
          const comptablePrincipale: IComptablePrincipale = { id: 123 };
          expectedResult = service.addComptablePrincipaleToCollectionIfMissing([], comptablePrincipale);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(comptablePrincipale);
        });

        it('should not add a ComptablePrincipale to an array that contains it', () => {
          const comptablePrincipale: IComptablePrincipale = { id: 123 };
          const comptablePrincipaleCollection: IComptablePrincipale[] = [
            {
              ...comptablePrincipale,
            },
            { id: 456 },
          ];
          expectedResult = service.addComptablePrincipaleToCollectionIfMissing(comptablePrincipaleCollection, comptablePrincipale);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ComptablePrincipale to an array that doesn't contain it", () => {
          const comptablePrincipale: IComptablePrincipale = { id: 123 };
          const comptablePrincipaleCollection: IComptablePrincipale[] = [{ id: 456 }];
          expectedResult = service.addComptablePrincipaleToCollectionIfMissing(comptablePrincipaleCollection, comptablePrincipale);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(comptablePrincipale);
        });

        it('should add only unique ComptablePrincipale to an array', () => {
          const comptablePrincipaleArray: IComptablePrincipale[] = [{ id: 123 }, { id: 456 }, { id: 23093 }];
          const comptablePrincipaleCollection: IComptablePrincipale[] = [{ id: 123 }];
          expectedResult = service.addComptablePrincipaleToCollectionIfMissing(comptablePrincipaleCollection, ...comptablePrincipaleArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const comptablePrincipale: IComptablePrincipale = { id: 123 };
          const comptablePrincipale2: IComptablePrincipale = { id: 456 };
          expectedResult = service.addComptablePrincipaleToCollectionIfMissing([], comptablePrincipale, comptablePrincipale2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(comptablePrincipale);
          expect(expectedResult).toContain(comptablePrincipale2);
        });

        it('should accept null and undefined values', () => {
          const comptablePrincipale: IComptablePrincipale = { id: 123 };
          expectedResult = service.addComptablePrincipaleToCollectionIfMissing([], null, comptablePrincipale, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(comptablePrincipale);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
