import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Classe } from 'app/entities/enumerations/classe.model';
import { ISalles, Salles } from '../salles.model';

import { SallesService } from './salles.service';

describe('Service Tests', () => {
  describe('Salles Service', () => {
    let service: SallesService;
    let httpMock: HttpTestingController;
    let elemDefault: ISalles;
    let expectedResult: ISalles | ISalles[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(SallesService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nomSalle: 'AAAAAAA',
        classe: Classe.ClassePhysique,
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

      it('should create a Salles', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Salles()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Salles', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nomSalle: 'BBBBBB',
            classe: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Salles', () => {
        const patchObject = Object.assign({}, new Salles());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Salles', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nomSalle: 'BBBBBB',
            classe: 'BBBBBB',
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

      it('should delete a Salles', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addSallesToCollectionIfMissing', () => {
        it('should add a Salles to an empty array', () => {
          const salles: ISalles = { id: 123 };
          expectedResult = service.addSallesToCollectionIfMissing([], salles);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(salles);
        });

        it('should not add a Salles to an array that contains it', () => {
          const salles: ISalles = { id: 123 };
          const sallesCollection: ISalles[] = [
            {
              ...salles,
            },
            { id: 456 },
          ];
          expectedResult = service.addSallesToCollectionIfMissing(sallesCollection, salles);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Salles to an array that doesn't contain it", () => {
          const salles: ISalles = { id: 123 };
          const sallesCollection: ISalles[] = [{ id: 456 }];
          expectedResult = service.addSallesToCollectionIfMissing(sallesCollection, salles);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(salles);
        });

        it('should add only unique Salles to an array', () => {
          const sallesArray: ISalles[] = [{ id: 123 }, { id: 456 }, { id: 49840 }];
          const sallesCollection: ISalles[] = [{ id: 123 }];
          expectedResult = service.addSallesToCollectionIfMissing(sallesCollection, ...sallesArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const salles: ISalles = { id: 123 };
          const salles2: ISalles = { id: 456 };
          expectedResult = service.addSallesToCollectionIfMissing([], salles, salles2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(salles);
          expect(expectedResult).toContain(salles2);
        });

        it('should accept null and undefined values', () => {
          const salles: ISalles = { id: 123 };
          expectedResult = service.addSallesToCollectionIfMissing([], null, salles, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(salles);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
