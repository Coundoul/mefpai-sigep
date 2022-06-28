import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IRequete, Requete } from '../requete.model';

import { RequeteService } from './requete.service';

describe('Service Tests', () => {
  describe('Requete Service', () => {
    let service: RequeteService;
    let httpMock: HttpTestingController;
    let elemDefault: IRequete;
    let expectedResult: IRequete | IRequete[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(RequeteService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        type: 'AAAAAAA',
        typePanne: 0,
        datePost: 0,
        description: 'AAAAAAA',
        etatTraite: false,
        dateLancement: currentDate,
        idPers: 0,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateLancement: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Requete', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateLancement: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateLancement: currentDate,
          },
          returnedFromService
        );

        service.create(new Requete()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Requete', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            type: 'BBBBBB',
            typePanne: 1,
            datePost: 1,
            description: 'BBBBBB',
            etatTraite: true,
            dateLancement: currentDate.format(DATE_TIME_FORMAT),
            idPers: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateLancement: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Requete', () => {
        const patchObject = Object.assign(
          {
            datePost: 1,
            description: 'BBBBBB',
          },
          new Requete()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            dateLancement: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Requete', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            type: 'BBBBBB',
            typePanne: 1,
            datePost: 1,
            description: 'BBBBBB',
            etatTraite: true,
            dateLancement: currentDate.format(DATE_TIME_FORMAT),
            idPers: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateLancement: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Requete', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addRequeteToCollectionIfMissing', () => {
        it('should add a Requete to an empty array', () => {
          const requete: IRequete = { id: 123 };
          expectedResult = service.addRequeteToCollectionIfMissing([], requete);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(requete);
        });

        it('should not add a Requete to an array that contains it', () => {
          const requete: IRequete = { id: 123 };
          const requeteCollection: IRequete[] = [
            {
              ...requete,
            },
            { id: 456 },
          ];
          expectedResult = service.addRequeteToCollectionIfMissing(requeteCollection, requete);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Requete to an array that doesn't contain it", () => {
          const requete: IRequete = { id: 123 };
          const requeteCollection: IRequete[] = [{ id: 456 }];
          expectedResult = service.addRequeteToCollectionIfMissing(requeteCollection, requete);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(requete);
        });

        it('should add only unique Requete to an array', () => {
          const requeteArray: IRequete[] = [{ id: 123 }, { id: 456 }, { id: 51733 }];
          const requeteCollection: IRequete[] = [{ id: 123 }];
          expectedResult = service.addRequeteToCollectionIfMissing(requeteCollection, ...requeteArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const requete: IRequete = { id: 123 };
          const requete2: IRequete = { id: 456 };
          expectedResult = service.addRequeteToCollectionIfMissing([], requete, requete2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(requete);
          expect(expectedResult).toContain(requete2);
        });

        it('should accept null and undefined values', () => {
          const requete: IRequete = { id: 123 };
          expectedResult = service.addRequeteToCollectionIfMissing([], null, requete, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(requete);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
