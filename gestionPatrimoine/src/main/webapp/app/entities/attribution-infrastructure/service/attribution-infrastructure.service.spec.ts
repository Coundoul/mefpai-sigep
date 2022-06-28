import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAttributionInfrastructure, AttributionInfrastructure } from '../attribution-infrastructure.model';

import { AttributionInfrastructureService } from './attribution-infrastructure.service';

describe('Service Tests', () => {
  describe('AttributionInfrastructure Service', () => {
    let service: AttributionInfrastructureService;
    let httpMock: HttpTestingController;
    let elemDefault: IAttributionInfrastructure;
    let expectedResult: IAttributionInfrastructure | IAttributionInfrastructure[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(AttributionInfrastructureService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        dateAttribution: currentDate,
        quantite: 0,
        idEquipement: 0,
        idPers: 0,
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

      it('should create a AttributionInfrastructure', () => {
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

        service.create(new AttributionInfrastructure()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a AttributionInfrastructure', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            dateAttribution: currentDate.format(DATE_TIME_FORMAT),
            quantite: 1,
            idEquipement: 1,
            idPers: 1,
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

      it('should partial update a AttributionInfrastructure', () => {
        const patchObject = Object.assign(
          {
            quantite: 1,
          },
          new AttributionInfrastructure()
        );

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

      it('should return a list of AttributionInfrastructure', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            dateAttribution: currentDate.format(DATE_TIME_FORMAT),
            quantite: 1,
            idEquipement: 1,
            idPers: 1,
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

      it('should delete a AttributionInfrastructure', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addAttributionInfrastructureToCollectionIfMissing', () => {
        it('should add a AttributionInfrastructure to an empty array', () => {
          const attributionInfrastructure: IAttributionInfrastructure = { id: 123 };
          expectedResult = service.addAttributionInfrastructureToCollectionIfMissing([], attributionInfrastructure);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(attributionInfrastructure);
        });

        it('should not add a AttributionInfrastructure to an array that contains it', () => {
          const attributionInfrastructure: IAttributionInfrastructure = { id: 123 };
          const attributionInfrastructureCollection: IAttributionInfrastructure[] = [
            {
              ...attributionInfrastructure,
            },
            { id: 456 },
          ];
          expectedResult = service.addAttributionInfrastructureToCollectionIfMissing(
            attributionInfrastructureCollection,
            attributionInfrastructure
          );
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a AttributionInfrastructure to an array that doesn't contain it", () => {
          const attributionInfrastructure: IAttributionInfrastructure = { id: 123 };
          const attributionInfrastructureCollection: IAttributionInfrastructure[] = [{ id: 456 }];
          expectedResult = service.addAttributionInfrastructureToCollectionIfMissing(
            attributionInfrastructureCollection,
            attributionInfrastructure
          );
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(attributionInfrastructure);
        });

        it('should add only unique AttributionInfrastructure to an array', () => {
          const attributionInfrastructureArray: IAttributionInfrastructure[] = [{ id: 123 }, { id: 456 }, { id: 19202 }];
          const attributionInfrastructureCollection: IAttributionInfrastructure[] = [{ id: 123 }];
          expectedResult = service.addAttributionInfrastructureToCollectionIfMissing(
            attributionInfrastructureCollection,
            ...attributionInfrastructureArray
          );
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const attributionInfrastructure: IAttributionInfrastructure = { id: 123 };
          const attributionInfrastructure2: IAttributionInfrastructure = { id: 456 };
          expectedResult = service.addAttributionInfrastructureToCollectionIfMissing(
            [],
            attributionInfrastructure,
            attributionInfrastructure2
          );
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(attributionInfrastructure);
          expect(expectedResult).toContain(attributionInfrastructure2);
        });

        it('should accept null and undefined values', () => {
          const attributionInfrastructure: IAttributionInfrastructure = { id: 123 };
          expectedResult = service.addAttributionInfrastructureToCollectionIfMissing([], null, attributionInfrastructure, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(attributionInfrastructure);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
