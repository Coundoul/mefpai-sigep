import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Sexe } from 'app/entities/enumerations/sexe.model';
import { IChefEtablissement, ChefEtablissement } from '../chef-etablissement.model';

import { ChefEtablissementService } from './chef-etablissement.service';

describe('Service Tests', () => {
  describe('ChefEtablissement Service', () => {
    let service: ChefEtablissementService;
    let httpMock: HttpTestingController;
    let elemDefault: IChefEtablissement;
    let expectedResult: IChefEtablissement | IChefEtablissement[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ChefEtablissementService);
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

      it('should create a ChefEtablissement', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ChefEtablissement()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ChefEtablissement', () => {
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

      it('should partial update a ChefEtablissement', () => {
        const patchObject = Object.assign(
          {
            nomPers: 'BBBBBB',
            prenomPers: 'BBBBBB',
          },
          new ChefEtablissement()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ChefEtablissement', () => {
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

      it('should delete a ChefEtablissement', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addChefEtablissementToCollectionIfMissing', () => {
        it('should add a ChefEtablissement to an empty array', () => {
          const chefEtablissement: IChefEtablissement = { id: 123 };
          expectedResult = service.addChefEtablissementToCollectionIfMissing([], chefEtablissement);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(chefEtablissement);
        });

        it('should not add a ChefEtablissement to an array that contains it', () => {
          const chefEtablissement: IChefEtablissement = { id: 123 };
          const chefEtablissementCollection: IChefEtablissement[] = [
            {
              ...chefEtablissement,
            },
            { id: 456 },
          ];
          expectedResult = service.addChefEtablissementToCollectionIfMissing(chefEtablissementCollection, chefEtablissement);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ChefEtablissement to an array that doesn't contain it", () => {
          const chefEtablissement: IChefEtablissement = { id: 123 };
          const chefEtablissementCollection: IChefEtablissement[] = [{ id: 456 }];
          expectedResult = service.addChefEtablissementToCollectionIfMissing(chefEtablissementCollection, chefEtablissement);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(chefEtablissement);
        });

        it('should add only unique ChefEtablissement to an array', () => {
          const chefEtablissementArray: IChefEtablissement[] = [{ id: 123 }, { id: 456 }, { id: 67742 }];
          const chefEtablissementCollection: IChefEtablissement[] = [{ id: 123 }];
          expectedResult = service.addChefEtablissementToCollectionIfMissing(chefEtablissementCollection, ...chefEtablissementArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const chefEtablissement: IChefEtablissement = { id: 123 };
          const chefEtablissement2: IChefEtablissement = { id: 456 };
          expectedResult = service.addChefEtablissementToCollectionIfMissing([], chefEtablissement, chefEtablissement2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(chefEtablissement);
          expect(expectedResult).toContain(chefEtablissement2);
        });

        it('should accept null and undefined values', () => {
          const chefEtablissement: IChefEtablissement = { id: 123 };
          expectedResult = service.addChefEtablissementToCollectionIfMissing([], null, chefEtablissement, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(chefEtablissement);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
