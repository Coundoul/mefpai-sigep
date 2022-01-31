import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Sexe } from 'app/entities/enumerations/sexe.model';
import { Direction } from 'app/entities/enumerations/direction.model';
import { IDetenteur, Detenteur } from '../detenteur.model';

import { DetenteurService } from './detenteur.service';

describe('Service Tests', () => {
  describe('Detenteur Service', () => {
    let service: DetenteurService;
    let httpMock: HttpTestingController;
    let elemDefault: IDetenteur;
    let expectedResult: IDetenteur | IDetenteur[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(DetenteurService);
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

      it('should create a Detenteur', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Detenteur()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Detenteur', () => {
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

      it('should partial update a Detenteur', () => {
        const patchObject = Object.assign(
          {
            mobile: 'BBBBBB',
          },
          new Detenteur()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Detenteur', () => {
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

      it('should delete a Detenteur', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addDetenteurToCollectionIfMissing', () => {
        it('should add a Detenteur to an empty array', () => {
          const detenteur: IDetenteur = { id: 123 };
          expectedResult = service.addDetenteurToCollectionIfMissing([], detenteur);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(detenteur);
        });

        it('should not add a Detenteur to an array that contains it', () => {
          const detenteur: IDetenteur = { id: 123 };
          const detenteurCollection: IDetenteur[] = [
            {
              ...detenteur,
            },
            { id: 456 },
          ];
          expectedResult = service.addDetenteurToCollectionIfMissing(detenteurCollection, detenteur);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Detenteur to an array that doesn't contain it", () => {
          const detenteur: IDetenteur = { id: 123 };
          const detenteurCollection: IDetenteur[] = [{ id: 456 }];
          expectedResult = service.addDetenteurToCollectionIfMissing(detenteurCollection, detenteur);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(detenteur);
        });

        it('should add only unique Detenteur to an array', () => {
          const detenteurArray: IDetenteur[] = [{ id: 123 }, { id: 456 }, { id: 56438 }];
          const detenteurCollection: IDetenteur[] = [{ id: 123 }];
          expectedResult = service.addDetenteurToCollectionIfMissing(detenteurCollection, ...detenteurArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const detenteur: IDetenteur = { id: 123 };
          const detenteur2: IDetenteur = { id: 456 };
          expectedResult = service.addDetenteurToCollectionIfMissing([], detenteur, detenteur2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(detenteur);
          expect(expectedResult).toContain(detenteur2);
        });

        it('should accept null and undefined values', () => {
          const detenteur: IDetenteur = { id: 123 };
          expectedResult = service.addDetenteurToCollectionIfMissing([], null, detenteur, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(detenteur);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
