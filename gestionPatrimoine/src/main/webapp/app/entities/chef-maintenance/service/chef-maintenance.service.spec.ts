import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Sexe } from 'app/entities/enumerations/sexe.model';
import { Direction } from 'app/entities/enumerations/direction.model';
import { IChefMaintenance, ChefMaintenance } from '../chef-maintenance.model';

import { ChefMaintenanceService } from './chef-maintenance.service';

describe('Service Tests', () => {
  describe('ChefMaintenance Service', () => {
    let service: ChefMaintenanceService;
    let httpMock: HttpTestingController;
    let elemDefault: IChefMaintenance;
    let expectedResult: IChefMaintenance | IChefMaintenance[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ChefMaintenanceService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nomPers: 'AAAAAAA',
        prenomPers: 'AAAAAAA',
        sexe: Sexe.Masculin,
        mobile: 'AAAAAAA',
        adresse: 'AAAAAAA',
        direction: Direction.DAGE,
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

      it('should create a ChefMaintenance', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ChefMaintenance()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ChefMaintenance', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nomPers: 'BBBBBB',
            prenomPers: 'BBBBBB',
            sexe: 'BBBBBB',
            mobile: 'BBBBBB',
            adresse: 'BBBBBB',
            direction: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ChefMaintenance', () => {
        const patchObject = Object.assign(
          {
            nomPers: 'BBBBBB',
            mobile: 'BBBBBB',
          },
          new ChefMaintenance()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ChefMaintenance', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nomPers: 'BBBBBB',
            prenomPers: 'BBBBBB',
            sexe: 'BBBBBB',
            mobile: 'BBBBBB',
            adresse: 'BBBBBB',
            direction: 'BBBBBB',
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

      it('should delete a ChefMaintenance', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addChefMaintenanceToCollectionIfMissing', () => {
        it('should add a ChefMaintenance to an empty array', () => {
          const chefMaintenance: IChefMaintenance = { id: 123 };
          expectedResult = service.addChefMaintenanceToCollectionIfMissing([], chefMaintenance);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(chefMaintenance);
        });

        it('should not add a ChefMaintenance to an array that contains it', () => {
          const chefMaintenance: IChefMaintenance = { id: 123 };
          const chefMaintenanceCollection: IChefMaintenance[] = [
            {
              ...chefMaintenance,
            },
            { id: 456 },
          ];
          expectedResult = service.addChefMaintenanceToCollectionIfMissing(chefMaintenanceCollection, chefMaintenance);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ChefMaintenance to an array that doesn't contain it", () => {
          const chefMaintenance: IChefMaintenance = { id: 123 };
          const chefMaintenanceCollection: IChefMaintenance[] = [{ id: 456 }];
          expectedResult = service.addChefMaintenanceToCollectionIfMissing(chefMaintenanceCollection, chefMaintenance);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(chefMaintenance);
        });

        it('should add only unique ChefMaintenance to an array', () => {
          const chefMaintenanceArray: IChefMaintenance[] = [{ id: 123 }, { id: 456 }, { id: 68700 }];
          const chefMaintenanceCollection: IChefMaintenance[] = [{ id: 123 }];
          expectedResult = service.addChefMaintenanceToCollectionIfMissing(chefMaintenanceCollection, ...chefMaintenanceArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const chefMaintenance: IChefMaintenance = { id: 123 };
          const chefMaintenance2: IChefMaintenance = { id: 456 };
          expectedResult = service.addChefMaintenanceToCollectionIfMissing([], chefMaintenance, chefMaintenance2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(chefMaintenance);
          expect(expectedResult).toContain(chefMaintenance2);
        });

        it('should accept null and undefined values', () => {
          const chefMaintenance: IChefMaintenance = { id: 123 };
          expectedResult = service.addChefMaintenanceToCollectionIfMissing([], null, chefMaintenance, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(chefMaintenance);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
