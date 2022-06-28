import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFiliereStabilise, FiliereStabilise } from '../filiere-stabilise.model';

import { FiliereStabiliseService } from './filiere-stabilise.service';

describe('Service Tests', () => {
  describe('FiliereStabilise Service', () => {
    let service: FiliereStabiliseService;
    let httpMock: HttpTestingController;
    let elemDefault: IFiliereStabilise;
    let expectedResult: IFiliereStabilise | IFiliereStabilise[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(FiliereStabiliseService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nomFiliere: 'AAAAAAA',
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

      it('should create a FiliereStabilise', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new FiliereStabilise()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a FiliereStabilise', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nomFiliere: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a FiliereStabilise', () => {
        const patchObject = Object.assign(
          {
            nomFiliere: 'BBBBBB',
          },
          new FiliereStabilise()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of FiliereStabilise', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nomFiliere: 'BBBBBB',
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

      it('should delete a FiliereStabilise', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addFiliereStabiliseToCollectionIfMissing', () => {
        it('should add a FiliereStabilise to an empty array', () => {
          const filiereStabilise: IFiliereStabilise = { id: 123 };
          expectedResult = service.addFiliereStabiliseToCollectionIfMissing([], filiereStabilise);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(filiereStabilise);
        });

        it('should not add a FiliereStabilise to an array that contains it', () => {
          const filiereStabilise: IFiliereStabilise = { id: 123 };
          const filiereStabiliseCollection: IFiliereStabilise[] = [
            {
              ...filiereStabilise,
            },
            { id: 456 },
          ];
          expectedResult = service.addFiliereStabiliseToCollectionIfMissing(filiereStabiliseCollection, filiereStabilise);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a FiliereStabilise to an array that doesn't contain it", () => {
          const filiereStabilise: IFiliereStabilise = { id: 123 };
          const filiereStabiliseCollection: IFiliereStabilise[] = [{ id: 456 }];
          expectedResult = service.addFiliereStabiliseToCollectionIfMissing(filiereStabiliseCollection, filiereStabilise);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(filiereStabilise);
        });

        it('should add only unique FiliereStabilise to an array', () => {
          const filiereStabiliseArray: IFiliereStabilise[] = [{ id: 123 }, { id: 456 }, { id: 21807 }];
          const filiereStabiliseCollection: IFiliereStabilise[] = [{ id: 123 }];
          expectedResult = service.addFiliereStabiliseToCollectionIfMissing(filiereStabiliseCollection, ...filiereStabiliseArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const filiereStabilise: IFiliereStabilise = { id: 123 };
          const filiereStabilise2: IFiliereStabilise = { id: 456 };
          expectedResult = service.addFiliereStabiliseToCollectionIfMissing([], filiereStabilise, filiereStabilise2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(filiereStabilise);
          expect(expectedResult).toContain(filiereStabilise2);
        });

        it('should accept null and undefined values', () => {
          const filiereStabilise: IFiliereStabilise = { id: 123 };
          expectedResult = service.addFiliereStabiliseToCollectionIfMissing([], null, filiereStabilise, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(filiereStabilise);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
