import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFicheTechniqueMaintenance, FicheTechniqueMaintenance } from '../fiche-technique-maintenance.model';

import { FicheTechniqueMaintenanceService } from './fiche-technique-maintenance.service';

describe('Service Tests', () => {
  describe('FicheTechniqueMaintenance Service', () => {
    let service: FicheTechniqueMaintenanceService;
    let httpMock: HttpTestingController;
    let elemDefault: IFicheTechniqueMaintenance;
    let expectedResult: IFicheTechniqueMaintenance | IFicheTechniqueMaintenance[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(FicheTechniqueMaintenanceService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        pieceJointe: 'AAAAAAA',
        idPers: 0,
        dateDepot: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateDepot: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a FicheTechniqueMaintenance', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateDepot: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateDepot: currentDate,
          },
          returnedFromService
        );

        service.create(new FicheTechniqueMaintenance()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a FicheTechniqueMaintenance', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            pieceJointe: 'BBBBBB',
            idPers: 1,
            dateDepot: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateDepot: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a FicheTechniqueMaintenance', () => {
        const patchObject = Object.assign(
          {
            idPers: 1,
          },
          new FicheTechniqueMaintenance()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            dateDepot: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of FicheTechniqueMaintenance', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            pieceJointe: 'BBBBBB',
            idPers: 1,
            dateDepot: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateDepot: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a FicheTechniqueMaintenance', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addFicheTechniqueMaintenanceToCollectionIfMissing', () => {
        it('should add a FicheTechniqueMaintenance to an empty array', () => {
          const ficheTechniqueMaintenance: IFicheTechniqueMaintenance = { id: 123 };
          expectedResult = service.addFicheTechniqueMaintenanceToCollectionIfMissing([], ficheTechniqueMaintenance);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(ficheTechniqueMaintenance);
        });

        it('should not add a FicheTechniqueMaintenance to an array that contains it', () => {
          const ficheTechniqueMaintenance: IFicheTechniqueMaintenance = { id: 123 };
          const ficheTechniqueMaintenanceCollection: IFicheTechniqueMaintenance[] = [
            {
              ...ficheTechniqueMaintenance,
            },
            { id: 456 },
          ];
          expectedResult = service.addFicheTechniqueMaintenanceToCollectionIfMissing(
            ficheTechniqueMaintenanceCollection,
            ficheTechniqueMaintenance
          );
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a FicheTechniqueMaintenance to an array that doesn't contain it", () => {
          const ficheTechniqueMaintenance: IFicheTechniqueMaintenance = { id: 123 };
          const ficheTechniqueMaintenanceCollection: IFicheTechniqueMaintenance[] = [{ id: 456 }];
          expectedResult = service.addFicheTechniqueMaintenanceToCollectionIfMissing(
            ficheTechniqueMaintenanceCollection,
            ficheTechniqueMaintenance
          );
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(ficheTechniqueMaintenance);
        });

        it('should add only unique FicheTechniqueMaintenance to an array', () => {
          const ficheTechniqueMaintenanceArray: IFicheTechniqueMaintenance[] = [{ id: 123 }, { id: 456 }, { id: 89170 }];
          const ficheTechniqueMaintenanceCollection: IFicheTechniqueMaintenance[] = [{ id: 123 }];
          expectedResult = service.addFicheTechniqueMaintenanceToCollectionIfMissing(
            ficheTechniqueMaintenanceCollection,
            ...ficheTechniqueMaintenanceArray
          );
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const ficheTechniqueMaintenance: IFicheTechniqueMaintenance = { id: 123 };
          const ficheTechniqueMaintenance2: IFicheTechniqueMaintenance = { id: 456 };
          expectedResult = service.addFicheTechniqueMaintenanceToCollectionIfMissing(
            [],
            ficheTechniqueMaintenance,
            ficheTechniqueMaintenance2
          );
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(ficheTechniqueMaintenance);
          expect(expectedResult).toContain(ficheTechniqueMaintenance2);
        });

        it('should accept null and undefined values', () => {
          const ficheTechniqueMaintenance: IFicheTechniqueMaintenance = { id: 123 };
          expectedResult = service.addFicheTechniqueMaintenanceToCollectionIfMissing([], null, ficheTechniqueMaintenance, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(ficheTechniqueMaintenance);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
