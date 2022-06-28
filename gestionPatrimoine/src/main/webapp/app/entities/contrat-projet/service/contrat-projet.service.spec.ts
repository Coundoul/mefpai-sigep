import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IContratProjet, ContratProjet } from '../contrat-projet.model';

import { ContratProjetService } from './contrat-projet.service';

describe('Service Tests', () => {
  describe('ContratProjet Service', () => {
    let service: ContratProjetService;
    let httpMock: HttpTestingController;
    let elemDefault: IContratProjet;
    let expectedResult: IContratProjet | IContratProjet[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ContratProjetService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nom: 'AAAAAAA',
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

      it('should create a ContratProjet', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ContratProjet()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ContratProjet', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nom: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ContratProjet', () => {
        const patchObject = Object.assign({}, new ContratProjet());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ContratProjet', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nom: 'BBBBBB',
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

      it('should delete a ContratProjet', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addContratProjetToCollectionIfMissing', () => {
        it('should add a ContratProjet to an empty array', () => {
          const contratProjet: IContratProjet = { id: 123 };
          expectedResult = service.addContratProjetToCollectionIfMissing([], contratProjet);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(contratProjet);
        });

        it('should not add a ContratProjet to an array that contains it', () => {
          const contratProjet: IContratProjet = { id: 123 };
          const contratProjetCollection: IContratProjet[] = [
            {
              ...contratProjet,
            },
            { id: 456 },
          ];
          expectedResult = service.addContratProjetToCollectionIfMissing(contratProjetCollection, contratProjet);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ContratProjet to an array that doesn't contain it", () => {
          const contratProjet: IContratProjet = { id: 123 };
          const contratProjetCollection: IContratProjet[] = [{ id: 456 }];
          expectedResult = service.addContratProjetToCollectionIfMissing(contratProjetCollection, contratProjet);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(contratProjet);
        });

        it('should add only unique ContratProjet to an array', () => {
          const contratProjetArray: IContratProjet[] = [{ id: 123 }, { id: 456 }, { id: 2448 }];
          const contratProjetCollection: IContratProjet[] = [{ id: 123 }];
          expectedResult = service.addContratProjetToCollectionIfMissing(contratProjetCollection, ...contratProjetArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const contratProjet: IContratProjet = { id: 123 };
          const contratProjet2: IContratProjet = { id: 456 };
          expectedResult = service.addContratProjetToCollectionIfMissing([], contratProjet, contratProjet2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(contratProjet);
          expect(expectedResult).toContain(contratProjet2);
        });

        it('should accept null and undefined values', () => {
          const contratProjet: IContratProjet = { id: 123 };
          expectedResult = service.addContratProjetToCollectionIfMissing([], null, contratProjet, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(contratProjet);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
