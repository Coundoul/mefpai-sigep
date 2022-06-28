import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFormateurs, Formateurs } from '../formateurs.model';

import { FormateursService } from './formateurs.service';

describe('Service Tests', () => {
  describe('Formateurs Service', () => {
    let service: FormateursService;
    let httpMock: HttpTestingController;
    let elemDefault: IFormateurs;
    let expectedResult: IFormateurs | IFormateurs[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(FormateursService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nomFormateur: 'AAAAAAA',
        prenomFormateur: 'AAAAAAA',
        email: 'AAAAAAA',
        numb1: 'AAAAAAA',
        numb2: 'AAAAAAA',
        adresse: 'AAAAAAA',
        ville: 'AAAAAAA',
        specialite: 'AAAAAAA',
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

      it('should create a Formateurs', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Formateurs()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Formateurs', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nomFormateur: 'BBBBBB',
            prenomFormateur: 'BBBBBB',
            email: 'BBBBBB',
            numb1: 'BBBBBB',
            numb2: 'BBBBBB',
            adresse: 'BBBBBB',
            ville: 'BBBBBB',
            specialite: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Formateurs', () => {
        const patchObject = Object.assign(
          {
            nomFormateur: 'BBBBBB',
            email: 'BBBBBB',
            adresse: 'BBBBBB',
            ville: 'BBBBBB',
            specialite: 'BBBBBB',
          },
          new Formateurs()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Formateurs', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nomFormateur: 'BBBBBB',
            prenomFormateur: 'BBBBBB',
            email: 'BBBBBB',
            numb1: 'BBBBBB',
            numb2: 'BBBBBB',
            adresse: 'BBBBBB',
            ville: 'BBBBBB',
            specialite: 'BBBBBB',
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

      it('should delete a Formateurs', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addFormateursToCollectionIfMissing', () => {
        it('should add a Formateurs to an empty array', () => {
          const formateurs: IFormateurs = { id: 123 };
          expectedResult = service.addFormateursToCollectionIfMissing([], formateurs);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(formateurs);
        });

        it('should not add a Formateurs to an array that contains it', () => {
          const formateurs: IFormateurs = { id: 123 };
          const formateursCollection: IFormateurs[] = [
            {
              ...formateurs,
            },
            { id: 456 },
          ];
          expectedResult = service.addFormateursToCollectionIfMissing(formateursCollection, formateurs);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Formateurs to an array that doesn't contain it", () => {
          const formateurs: IFormateurs = { id: 123 };
          const formateursCollection: IFormateurs[] = [{ id: 456 }];
          expectedResult = service.addFormateursToCollectionIfMissing(formateursCollection, formateurs);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(formateurs);
        });

        it('should add only unique Formateurs to an array', () => {
          const formateursArray: IFormateurs[] = [{ id: 123 }, { id: 456 }, { id: 26009 }];
          const formateursCollection: IFormateurs[] = [{ id: 123 }];
          expectedResult = service.addFormateursToCollectionIfMissing(formateursCollection, ...formateursArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const formateurs: IFormateurs = { id: 123 };
          const formateurs2: IFormateurs = { id: 456 };
          expectedResult = service.addFormateursToCollectionIfMissing([], formateurs, formateurs2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(formateurs);
          expect(expectedResult).toContain(formateurs2);
        });

        it('should accept null and undefined values', () => {
          const formateurs: IFormateurs = { id: 123 };
          expectedResult = service.addFormateursToCollectionIfMissing([], null, formateurs, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(formateurs);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
