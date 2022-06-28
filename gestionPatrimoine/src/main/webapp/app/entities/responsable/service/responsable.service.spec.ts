import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IResponsable, Responsable } from '../responsable.model';

import { ResponsableService } from './responsable.service';

describe('Service Tests', () => {
  describe('Responsable Service', () => {
    let service: ResponsableService;
    let httpMock: HttpTestingController;
    let elemDefault: IResponsable;
    let expectedResult: IResponsable | IResponsable[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ResponsableService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nomResponsable: 'AAAAAAA',
        prenomResponsable: 'AAAAAAA',
        email: 'AAAAAAA',
        specialite: 'AAAAAAA',
        numb1: 'AAAAAAA',
        numb2: 'AAAAAAA',
        raisonSocial: 'AAAAAAA',
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

      it('should create a Responsable', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Responsable()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Responsable', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nomResponsable: 'BBBBBB',
            prenomResponsable: 'BBBBBB',
            email: 'BBBBBB',
            specialite: 'BBBBBB',
            numb1: 'BBBBBB',
            numb2: 'BBBBBB',
            raisonSocial: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Responsable', () => {
        const patchObject = Object.assign(
          {
            nomResponsable: 'BBBBBB',
            prenomResponsable: 'BBBBBB',
            numb1: 'BBBBBB',
            raisonSocial: 'BBBBBB',
          },
          new Responsable()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Responsable', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nomResponsable: 'BBBBBB',
            prenomResponsable: 'BBBBBB',
            email: 'BBBBBB',
            specialite: 'BBBBBB',
            numb1: 'BBBBBB',
            numb2: 'BBBBBB',
            raisonSocial: 'BBBBBB',
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

      it('should delete a Responsable', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addResponsableToCollectionIfMissing', () => {
        it('should add a Responsable to an empty array', () => {
          const responsable: IResponsable = { id: 123 };
          expectedResult = service.addResponsableToCollectionIfMissing([], responsable);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(responsable);
        });

        it('should not add a Responsable to an array that contains it', () => {
          const responsable: IResponsable = { id: 123 };
          const responsableCollection: IResponsable[] = [
            {
              ...responsable,
            },
            { id: 456 },
          ];
          expectedResult = service.addResponsableToCollectionIfMissing(responsableCollection, responsable);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Responsable to an array that doesn't contain it", () => {
          const responsable: IResponsable = { id: 123 };
          const responsableCollection: IResponsable[] = [{ id: 456 }];
          expectedResult = service.addResponsableToCollectionIfMissing(responsableCollection, responsable);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(responsable);
        });

        it('should add only unique Responsable to an array', () => {
          const responsableArray: IResponsable[] = [{ id: 123 }, { id: 456 }, { id: 58372 }];
          const responsableCollection: IResponsable[] = [{ id: 123 }];
          expectedResult = service.addResponsableToCollectionIfMissing(responsableCollection, ...responsableArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const responsable: IResponsable = { id: 123 };
          const responsable2: IResponsable = { id: 456 };
          expectedResult = service.addResponsableToCollectionIfMissing([], responsable, responsable2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(responsable);
          expect(expectedResult).toContain(responsable2);
        });

        it('should accept null and undefined values', () => {
          const responsable: IResponsable = { id: 123 };
          expectedResult = service.addResponsableToCollectionIfMissing([], null, responsable, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(responsable);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
