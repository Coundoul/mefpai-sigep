import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { TypeProjet } from 'app/entities/enumerations/type-projet.model';
import { IProjets, Projets } from '../projets.model';

import { ProjetsService } from './projets.service';

describe('Service Tests', () => {
  describe('Projets Service', () => {
    let service: ProjetsService;
    let httpMock: HttpTestingController;
    let elemDefault: IProjets;
    let expectedResult: IProjets | IProjets[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ProjetsService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        typeProjet: TypeProjet.Construction,
        nomProjet: 'AAAAAAA',
        dateDebut: currentDate,
        dateFin: currentDate,
        description: 'AAAAAAA',
        extension: false,
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

      it('should create a Projets', () => {
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

        service.create(new Projets()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Projets', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            typeProjet: 'BBBBBB',
            nomProjet: 'BBBBBB',
            dateDebut: currentDate.format(DATE_TIME_FORMAT),
            dateFin: currentDate.format(DATE_TIME_FORMAT),
            description: 'BBBBBB',
            extension: true,
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

      it('should partial update a Projets', () => {
        const patchObject = Object.assign(
          {
            typeProjet: 'BBBBBB',
            dateDebut: currentDate.format(DATE_TIME_FORMAT),
          },
          new Projets()
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

      it('should return a list of Projets', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            typeProjet: 'BBBBBB',
            nomProjet: 'BBBBBB',
            dateDebut: currentDate.format(DATE_TIME_FORMAT),
            dateFin: currentDate.format(DATE_TIME_FORMAT),
            description: 'BBBBBB',
            extension: true,
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

      it('should delete a Projets', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addProjetsToCollectionIfMissing', () => {
        it('should add a Projets to an empty array', () => {
          const projets: IProjets = { id: 123 };
          expectedResult = service.addProjetsToCollectionIfMissing([], projets);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(projets);
        });

        it('should not add a Projets to an array that contains it', () => {
          const projets: IProjets = { id: 123 };
          const projetsCollection: IProjets[] = [
            {
              ...projets,
            },
            { id: 456 },
          ];
          expectedResult = service.addProjetsToCollectionIfMissing(projetsCollection, projets);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Projets to an array that doesn't contain it", () => {
          const projets: IProjets = { id: 123 };
          const projetsCollection: IProjets[] = [{ id: 456 }];
          expectedResult = service.addProjetsToCollectionIfMissing(projetsCollection, projets);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(projets);
        });

        it('should add only unique Projets to an array', () => {
          const projetsArray: IProjets[] = [{ id: 123 }, { id: 456 }, { id: 22284 }];
          const projetsCollection: IProjets[] = [{ id: 123 }];
          expectedResult = service.addProjetsToCollectionIfMissing(projetsCollection, ...projetsArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const projets: IProjets = { id: 123 };
          const projets2: IProjets = { id: 456 };
          expectedResult = service.addProjetsToCollectionIfMissing([], projets, projets2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(projets);
          expect(expectedResult).toContain(projets2);
        });

        it('should accept null and undefined values', () => {
          const projets: IProjets = { id: 123 };
          expectedResult = service.addProjetsToCollectionIfMissing([], null, projets, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(projets);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
