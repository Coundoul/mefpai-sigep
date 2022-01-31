import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { TypeBon } from 'app/entities/enumerations/type-bon.model';
import { IBon, Bon } from '../bon.model';

import { BonService } from './bon.service';

describe('Service Tests', () => {
  describe('Bon Service', () => {
    let service: BonService;
    let httpMock: HttpTestingController;
    let elemDefault: IBon;
    let expectedResult: IBon | IBon[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(BonService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        typeBon: TypeBon.Entree,
        quantiteLivre: 0,
        quantiteCommande: 0,
        dateCreation: currentDate,
        etat: false,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateCreation: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Bon', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateCreation: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateCreation: currentDate,
          },
          returnedFromService
        );

        service.create(new Bon()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Bon', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            typeBon: 'BBBBBB',
            quantiteLivre: 1,
            quantiteCommande: 1,
            dateCreation: currentDate.format(DATE_TIME_FORMAT),
            etat: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateCreation: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Bon', () => {
        const patchObject = Object.assign(
          {
            typeBon: 'BBBBBB',
            quantiteLivre: 1,
          },
          new Bon()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            dateCreation: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Bon', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            typeBon: 'BBBBBB',
            quantiteLivre: 1,
            quantiteCommande: 1,
            dateCreation: currentDate.format(DATE_TIME_FORMAT),
            etat: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateCreation: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Bon', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addBonToCollectionIfMissing', () => {
        it('should add a Bon to an empty array', () => {
          const bon: IBon = { id: 123 };
          expectedResult = service.addBonToCollectionIfMissing([], bon);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(bon);
        });

        it('should not add a Bon to an array that contains it', () => {
          const bon: IBon = { id: 123 };
          const bonCollection: IBon[] = [
            {
              ...bon,
            },
            { id: 456 },
          ];
          expectedResult = service.addBonToCollectionIfMissing(bonCollection, bon);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Bon to an array that doesn't contain it", () => {
          const bon: IBon = { id: 123 };
          const bonCollection: IBon[] = [{ id: 456 }];
          expectedResult = service.addBonToCollectionIfMissing(bonCollection, bon);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(bon);
        });

        it('should add only unique Bon to an array', () => {
          const bonArray: IBon[] = [{ id: 123 }, { id: 456 }, { id: 87508 }];
          const bonCollection: IBon[] = [{ id: 123 }];
          expectedResult = service.addBonToCollectionIfMissing(bonCollection, ...bonArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const bon: IBon = { id: 123 };
          const bon2: IBon = { id: 456 };
          expectedResult = service.addBonToCollectionIfMissing([], bon, bon2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(bon);
          expect(expectedResult).toContain(bon2);
        });

        it('should accept null and undefined values', () => {
          const bon: IBon = { id: 123 };
          expectedResult = service.addBonToCollectionIfMissing([], null, bon, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(bon);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
