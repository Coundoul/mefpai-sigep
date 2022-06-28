import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Sexe } from 'app/entities/enumerations/sexe.model';
import { Direction } from 'app/entities/enumerations/direction.model';
import { ITechnicien, Technicien } from '../technicien.model';

import { TechnicienService } from './technicien.service';

describe('Service Tests', () => {
  describe('Technicien Service', () => {
    let service: TechnicienService;
    let httpMock: HttpTestingController;
    let elemDefault: ITechnicien;
    let expectedResult: ITechnicien | ITechnicien[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(TechnicienService);
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

      it('should create a Technicien', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Technicien()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Technicien', () => {
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

      it('should partial update a Technicien', () => {
        const patchObject = Object.assign(
          {
            prenomPers: 'BBBBBB',
            sexe: 'BBBBBB',
            direction: 'BBBBBB',
          },
          new Technicien()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Technicien', () => {
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

      it('should delete a Technicien', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addTechnicienToCollectionIfMissing', () => {
        it('should add a Technicien to an empty array', () => {
          const technicien: ITechnicien = { id: 123 };
          expectedResult = service.addTechnicienToCollectionIfMissing([], technicien);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(technicien);
        });

        it('should not add a Technicien to an array that contains it', () => {
          const technicien: ITechnicien = { id: 123 };
          const technicienCollection: ITechnicien[] = [
            {
              ...technicien,
            },
            { id: 456 },
          ];
          expectedResult = service.addTechnicienToCollectionIfMissing(technicienCollection, technicien);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Technicien to an array that doesn't contain it", () => {
          const technicien: ITechnicien = { id: 123 };
          const technicienCollection: ITechnicien[] = [{ id: 456 }];
          expectedResult = service.addTechnicienToCollectionIfMissing(technicienCollection, technicien);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(technicien);
        });

        it('should add only unique Technicien to an array', () => {
          const technicienArray: ITechnicien[] = [{ id: 123 }, { id: 456 }, { id: 83951 }];
          const technicienCollection: ITechnicien[] = [{ id: 123 }];
          expectedResult = service.addTechnicienToCollectionIfMissing(technicienCollection, ...technicienArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const technicien: ITechnicien = { id: 123 };
          const technicien2: ITechnicien = { id: 456 };
          expectedResult = service.addTechnicienToCollectionIfMissing([], technicien, technicien2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(technicien);
          expect(expectedResult).toContain(technicien2);
        });

        it('should accept null and undefined values', () => {
          const technicien: ITechnicien = { id: 123 };
          expectedResult = service.addTechnicienToCollectionIfMissing([], null, technicien, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(technicien);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
