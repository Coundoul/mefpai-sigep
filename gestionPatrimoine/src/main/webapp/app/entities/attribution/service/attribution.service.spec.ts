import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAttribution, Attribution } from '../attribution.model';

import { AttributionService } from './attribution.service';

describe('Service Tests', () => {
  describe('Attribution Service', () => {
    let service: AttributionService;
    let httpMock: HttpTestingController;
    let elemDefault: IAttribution;
    let expectedResult: IAttribution | IAttribution[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(AttributionService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        quantiteAffecter: 0,
        idPers: 0,
        dateAffectation: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateAffectation: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Attribution', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateAffectation: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateAffectation: currentDate,
          },
          returnedFromService
        );

        service.create(new Attribution()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Attribution', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            quantiteAffecter: 1,
            idPers: 1,
            dateAffectation: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateAffectation: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Attribution', () => {
        const patchObject = Object.assign(
          {
            quantiteAffecter: 1,
            idPers: 1,
            dateAffectation: currentDate.format(DATE_TIME_FORMAT),
          },
          new Attribution()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            dateAffectation: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Attribution', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            quantiteAffecter: 1,
            idPers: 1,
            dateAffectation: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateAffectation: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Attribution', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addAttributionToCollectionIfMissing', () => {
        it('should add a Attribution to an empty array', () => {
          const attribution: IAttribution = { id: 123 };
          expectedResult = service.addAttributionToCollectionIfMissing([], attribution);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(attribution);
        });

        it('should not add a Attribution to an array that contains it', () => {
          const attribution: IAttribution = { id: 123 };
          const attributionCollection: IAttribution[] = [
            {
              ...attribution,
            },
            { id: 456 },
          ];
          expectedResult = service.addAttributionToCollectionIfMissing(attributionCollection, attribution);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Attribution to an array that doesn't contain it", () => {
          const attribution: IAttribution = { id: 123 };
          const attributionCollection: IAttribution[] = [{ id: 456 }];
          expectedResult = service.addAttributionToCollectionIfMissing(attributionCollection, attribution);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(attribution);
        });

        it('should add only unique Attribution to an array', () => {
          const attributionArray: IAttribution[] = [{ id: 123 }, { id: 456 }, { id: 32925 }];
          const attributionCollection: IAttribution[] = [{ id: 123 }];
          expectedResult = service.addAttributionToCollectionIfMissing(attributionCollection, ...attributionArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const attribution: IAttribution = { id: 123 };
          const attribution2: IAttribution = { id: 456 };
          expectedResult = service.addAttributionToCollectionIfMissing([], attribution, attribution2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(attribution);
          expect(expectedResult).toContain(attribution2);
        });

        it('should accept null and undefined values', () => {
          const attribution: IAttribution = { id: 123 };
          expectedResult = service.addAttributionToCollectionIfMissing([], null, attribution, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(attribution);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
