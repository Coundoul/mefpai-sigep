import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { Type } from 'app/entities/enumerations/type.model';
import { IAffectations, Affectations } from '../affectations.model';

import { AffectationsService } from './affectations.service';

describe('Service Tests', () => {
  describe('Affectations Service', () => {
    let service: AffectationsService;
    let httpMock: HttpTestingController;
    let elemDefault: IAffectations;
    let expectedResult: IAffectations | IAffectations[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(AffectationsService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        quantiteAffecter: 0,
        typeAttribution: Type.ReAffectation,
        idPers: 0,
        dateAttribution: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateAttribution: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Affectations', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateAttribution: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateAttribution: currentDate,
          },
          returnedFromService
        );

        service.create(new Affectations()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Affectations', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            quantiteAffecter: 1,
            typeAttribution: 'BBBBBB',
            idPers: 1,
            dateAttribution: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateAttribution: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Affectations', () => {
        const patchObject = Object.assign({}, new Affectations());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            dateAttribution: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Affectations', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            quantiteAffecter: 1,
            typeAttribution: 'BBBBBB',
            idPers: 1,
            dateAttribution: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateAttribution: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Affectations', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addAffectationsToCollectionIfMissing', () => {
        it('should add a Affectations to an empty array', () => {
          const affectations: IAffectations = { id: 123 };
          expectedResult = service.addAffectationsToCollectionIfMissing([], affectations);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(affectations);
        });

        it('should not add a Affectations to an array that contains it', () => {
          const affectations: IAffectations = { id: 123 };
          const affectationsCollection: IAffectations[] = [
            {
              ...affectations,
            },
            { id: 456 },
          ];
          expectedResult = service.addAffectationsToCollectionIfMissing(affectationsCollection, affectations);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Affectations to an array that doesn't contain it", () => {
          const affectations: IAffectations = { id: 123 };
          const affectationsCollection: IAffectations[] = [{ id: 456 }];
          expectedResult = service.addAffectationsToCollectionIfMissing(affectationsCollection, affectations);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(affectations);
        });

        it('should add only unique Affectations to an array', () => {
          const affectationsArray: IAffectations[] = [{ id: 123 }, { id: 456 }, { id: 88157 }];
          const affectationsCollection: IAffectations[] = [{ id: 123 }];
          expectedResult = service.addAffectationsToCollectionIfMissing(affectationsCollection, ...affectationsArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const affectations: IAffectations = { id: 123 };
          const affectations2: IAffectations = { id: 456 };
          expectedResult = service.addAffectationsToCollectionIfMissing([], affectations, affectations2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(affectations);
          expect(expectedResult).toContain(affectations2);
        });

        it('should accept null and undefined values', () => {
          const affectations: IAffectations = { id: 123 };
          expectedResult = service.addAffectationsToCollectionIfMissing([], null, affectations, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(affectations);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
