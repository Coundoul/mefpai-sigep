import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IProjetAttribution, ProjetAttribution } from '../projet-attribution.model';

import { ProjetAttributionService } from './projet-attribution.service';

describe('Service Tests', () => {
  describe('ProjetAttribution Service', () => {
    let service: ProjetAttributionService;
    let httpMock: HttpTestingController;
    let elemDefault: IProjetAttribution;
    let expectedResult: IProjetAttribution | IProjetAttribution[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ProjetAttributionService);
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

      it('should create a ProjetAttribution', () => {
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

        service.create(new ProjetAttribution()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ProjetAttribution', () => {
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

      it('should partial update a ProjetAttribution', () => {
        const patchObject = Object.assign(
          {
            quantite: 1,
            idEquipement: 1,
          },
          new ProjetAttribution()
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

      it('should return a list of ProjetAttribution', () => {
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

      it('should delete a ProjetAttribution', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addProjetAttributionToCollectionIfMissing', () => {
        it('should add a ProjetAttribution to an empty array', () => {
          const projetAttribution: IProjetAttribution = { id: 123 };
          expectedResult = service.addProjetAttributionToCollectionIfMissing([], projetAttribution);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(projetAttribution);
        });

        it('should not add a ProjetAttribution to an array that contains it', () => {
          const projetAttribution: IProjetAttribution = { id: 123 };
          const projetAttributionCollection: IProjetAttribution[] = [
            {
              ...projetAttribution,
            },
            { id: 456 },
          ];
          expectedResult = service.addProjetAttributionToCollectionIfMissing(projetAttributionCollection, projetAttribution);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ProjetAttribution to an array that doesn't contain it", () => {
          const projetAttribution: IProjetAttribution = { id: 123 };
          const projetAttributionCollection: IProjetAttribution[] = [{ id: 456 }];
          expectedResult = service.addProjetAttributionToCollectionIfMissing(projetAttributionCollection, projetAttribution);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(projetAttribution);
        });

        it('should add only unique ProjetAttribution to an array', () => {
          const projetAttributionArray: IProjetAttribution[] = [{ id: 123 }, { id: 456 }, { id: 57883 }];
          const projetAttributionCollection: IProjetAttribution[] = [{ id: 123 }];
          expectedResult = service.addProjetAttributionToCollectionIfMissing(projetAttributionCollection, ...projetAttributionArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const projetAttribution: IProjetAttribution = { id: 123 };
          const projetAttribution2: IProjetAttribution = { id: 456 };
          expectedResult = service.addProjetAttributionToCollectionIfMissing([], projetAttribution, projetAttribution2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(projetAttribution);
          expect(expectedResult).toContain(projetAttribution2);
        });

        it('should accept null and undefined values', () => {
          const projetAttribution: IProjetAttribution = { id: 123 };
          expectedResult = service.addProjetAttributionToCollectionIfMissing([], null, projetAttribution, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(projetAttribution);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
