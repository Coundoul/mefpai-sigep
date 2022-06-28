import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IEtapes, Etapes } from '../etapes.model';

import { EtapesService } from './etapes.service';

describe('Service Tests', () => {
  describe('Etapes Service', () => {
    let service: EtapesService;
    let httpMock: HttpTestingController;
    let elemDefault: IEtapes;
    let expectedResult: IEtapes | IEtapes[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(EtapesService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        dateDebut: currentDate,
        dateFin: currentDate,
        nomTache: 'AAAAAAA',
        duration: 'PT1S',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateDebut: currentDate.format(DATE_TIME_FORMAT),
            dateFin: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Etapes', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateDebut: currentDate.format(DATE_TIME_FORMAT),
            dateFin: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateDebut: currentDate,
            dateFin: currentDate,
          },
          returnedFromService
        );

        service.create(new Etapes()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Etapes', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            dateDebut: currentDate.format(DATE_TIME_FORMAT),
            dateFin: currentDate.format(DATE_TIME_FORMAT),
            nomTache: 'BBBBBB',
            duration: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateDebut: currentDate,
            dateFin: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Etapes', () => {
        const patchObject = Object.assign(
          {
            dateDebut: currentDate.format(DATE_TIME_FORMAT),
            dateFin: currentDate.format(DATE_TIME_FORMAT),
            duration: 'BBBBBB',
          },
          new Etapes()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            dateDebut: currentDate,
            dateFin: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Etapes', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            dateDebut: currentDate.format(DATE_TIME_FORMAT),
            dateFin: currentDate.format(DATE_TIME_FORMAT),
            nomTache: 'BBBBBB',
            duration: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateDebut: currentDate,
            dateFin: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Etapes', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addEtapesToCollectionIfMissing', () => {
        it('should add a Etapes to an empty array', () => {
          const etapes: IEtapes = { id: 123 };
          expectedResult = service.addEtapesToCollectionIfMissing([], etapes);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(etapes);
        });

        it('should not add a Etapes to an array that contains it', () => {
          const etapes: IEtapes = { id: 123 };
          const etapesCollection: IEtapes[] = [
            {
              ...etapes,
            },
            { id: 456 },
          ];
          expectedResult = service.addEtapesToCollectionIfMissing(etapesCollection, etapes);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Etapes to an array that doesn't contain it", () => {
          const etapes: IEtapes = { id: 123 };
          const etapesCollection: IEtapes[] = [{ id: 456 }];
          expectedResult = service.addEtapesToCollectionIfMissing(etapesCollection, etapes);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(etapes);
        });

        it('should add only unique Etapes to an array', () => {
          const etapesArray: IEtapes[] = [{ id: 123 }, { id: 456 }, { id: 45099 }];
          const etapesCollection: IEtapes[] = [{ id: 123 }];
          expectedResult = service.addEtapesToCollectionIfMissing(etapesCollection, ...etapesArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const etapes: IEtapes = { id: 123 };
          const etapes2: IEtapes = { id: 456 };
          expectedResult = service.addEtapesToCollectionIfMissing([], etapes, etapes2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(etapes);
          expect(expectedResult).toContain(etapes2);
        });

        it('should accept null and undefined values', () => {
          const etapes: IEtapes = { id: 123 };
          expectedResult = service.addEtapesToCollectionIfMissing([], null, etapes, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(etapes);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
