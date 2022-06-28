import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Sexe } from 'app/entities/enumerations/sexe.model';
import { Direction } from 'app/entities/enumerations/direction.model';
import { IChefProjet, ChefProjet } from '../chef-projet.model';

import { ChefProjetService } from './chef-projet.service';

describe('Service Tests', () => {
  describe('ChefProjet Service', () => {
    let service: ChefProjetService;
    let httpMock: HttpTestingController;
    let elemDefault: IChefProjet;
    let expectedResult: IChefProjet | IChefProjet[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ChefProjetService);
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

      it('should create a ChefProjet', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ChefProjet()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ChefProjet', () => {
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

      it('should partial update a ChefProjet', () => {
        const patchObject = Object.assign(
          {
            prenomPers: 'BBBBBB',
            mobile: 'BBBBBB',
            adresse: 'BBBBBB',
          },
          new ChefProjet()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ChefProjet', () => {
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

      it('should delete a ChefProjet', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addChefProjetToCollectionIfMissing', () => {
        it('should add a ChefProjet to an empty array', () => {
          const chefProjet: IChefProjet = { id: 123 };
          expectedResult = service.addChefProjetToCollectionIfMissing([], chefProjet);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(chefProjet);
        });

        it('should not add a ChefProjet to an array that contains it', () => {
          const chefProjet: IChefProjet = { id: 123 };
          const chefProjetCollection: IChefProjet[] = [
            {
              ...chefProjet,
            },
            { id: 456 },
          ];
          expectedResult = service.addChefProjetToCollectionIfMissing(chefProjetCollection, chefProjet);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ChefProjet to an array that doesn't contain it", () => {
          const chefProjet: IChefProjet = { id: 123 };
          const chefProjetCollection: IChefProjet[] = [{ id: 456 }];
          expectedResult = service.addChefProjetToCollectionIfMissing(chefProjetCollection, chefProjet);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(chefProjet);
        });

        it('should add only unique ChefProjet to an array', () => {
          const chefProjetArray: IChefProjet[] = [{ id: 123 }, { id: 456 }, { id: 31249 }];
          const chefProjetCollection: IChefProjet[] = [{ id: 123 }];
          expectedResult = service.addChefProjetToCollectionIfMissing(chefProjetCollection, ...chefProjetArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const chefProjet: IChefProjet = { id: 123 };
          const chefProjet2: IChefProjet = { id: 456 };
          expectedResult = service.addChefProjetToCollectionIfMissing([], chefProjet, chefProjet2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(chefProjet);
          expect(expectedResult).toContain(chefProjet2);
        });

        it('should accept null and undefined values', () => {
          const chefProjet: IChefProjet = { id: 123 };
          expectedResult = service.addChefProjetToCollectionIfMissing([], null, chefProjet, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(chefProjet);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
