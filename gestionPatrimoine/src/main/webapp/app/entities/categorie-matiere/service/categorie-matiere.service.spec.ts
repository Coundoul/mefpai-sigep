import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICategorieMatiere, CategorieMatiere } from '../categorie-matiere.model';

import { CategorieMatiereService } from './categorie-matiere.service';

describe('Service Tests', () => {
  describe('CategorieMatiere Service', () => {
    let service: CategorieMatiereService;
    let httpMock: HttpTestingController;
    let elemDefault: ICategorieMatiere;
    let expectedResult: ICategorieMatiere | ICategorieMatiere[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CategorieMatiereService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        categorie: 'AAAAAAA',
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

      it('should create a CategorieMatiere', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new CategorieMatiere()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a CategorieMatiere', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            categorie: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a CategorieMatiere', () => {
        const patchObject = Object.assign({}, new CategorieMatiere());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of CategorieMatiere', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            categorie: 'BBBBBB',
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

      it('should delete a CategorieMatiere', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCategorieMatiereToCollectionIfMissing', () => {
        it('should add a CategorieMatiere to an empty array', () => {
          const categorieMatiere: ICategorieMatiere = { id: 123 };
          expectedResult = service.addCategorieMatiereToCollectionIfMissing([], categorieMatiere);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(categorieMatiere);
        });

        it('should not add a CategorieMatiere to an array that contains it', () => {
          const categorieMatiere: ICategorieMatiere = { id: 123 };
          const categorieMatiereCollection: ICategorieMatiere[] = [
            {
              ...categorieMatiere,
            },
            { id: 456 },
          ];
          expectedResult = service.addCategorieMatiereToCollectionIfMissing(categorieMatiereCollection, categorieMatiere);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a CategorieMatiere to an array that doesn't contain it", () => {
          const categorieMatiere: ICategorieMatiere = { id: 123 };
          const categorieMatiereCollection: ICategorieMatiere[] = [{ id: 456 }];
          expectedResult = service.addCategorieMatiereToCollectionIfMissing(categorieMatiereCollection, categorieMatiere);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(categorieMatiere);
        });

        it('should add only unique CategorieMatiere to an array', () => {
          const categorieMatiereArray: ICategorieMatiere[] = [{ id: 123 }, { id: 456 }, { id: 18613 }];
          const categorieMatiereCollection: ICategorieMatiere[] = [{ id: 123 }];
          expectedResult = service.addCategorieMatiereToCollectionIfMissing(categorieMatiereCollection, ...categorieMatiereArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const categorieMatiere: ICategorieMatiere = { id: 123 };
          const categorieMatiere2: ICategorieMatiere = { id: 456 };
          expectedResult = service.addCategorieMatiereToCollectionIfMissing([], categorieMatiere, categorieMatiere2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(categorieMatiere);
          expect(expectedResult).toContain(categorieMatiere2);
        });

        it('should accept null and undefined values', () => {
          const categorieMatiere: ICategorieMatiere = { id: 123 };
          expectedResult = service.addCategorieMatiereToCollectionIfMissing([], null, categorieMatiere, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(categorieMatiere);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
