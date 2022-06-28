import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDetailSortie, DetailSortie } from '../detail-sortie.model';

import { DetailSortieService } from './detail-sortie.service';

describe('Service Tests', () => {
  describe('DetailSortie Service', () => {
    let service: DetailSortieService;
    let httpMock: HttpTestingController;
    let elemDefault: IDetailSortie;
    let expectedResult: IDetailSortie | IDetailSortie[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(DetailSortieService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        pieceJointe: 'AAAAAAA',
        idPers: 0,
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

      it('should create a DetailSortie', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new DetailSortie()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a DetailSortie', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            pieceJointe: 'BBBBBB',
            idPers: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a DetailSortie', () => {
        const patchObject = Object.assign(
          {
            idPers: 1,
          },
          new DetailSortie()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of DetailSortie', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            pieceJointe: 'BBBBBB',
            idPers: 1,
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

      it('should delete a DetailSortie', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addDetailSortieToCollectionIfMissing', () => {
        it('should add a DetailSortie to an empty array', () => {
          const detailSortie: IDetailSortie = { id: 123 };
          expectedResult = service.addDetailSortieToCollectionIfMissing([], detailSortie);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(detailSortie);
        });

        it('should not add a DetailSortie to an array that contains it', () => {
          const detailSortie: IDetailSortie = { id: 123 };
          const detailSortieCollection: IDetailSortie[] = [
            {
              ...detailSortie,
            },
            { id: 456 },
          ];
          expectedResult = service.addDetailSortieToCollectionIfMissing(detailSortieCollection, detailSortie);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a DetailSortie to an array that doesn't contain it", () => {
          const detailSortie: IDetailSortie = { id: 123 };
          const detailSortieCollection: IDetailSortie[] = [{ id: 456 }];
          expectedResult = service.addDetailSortieToCollectionIfMissing(detailSortieCollection, detailSortie);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(detailSortie);
        });

        it('should add only unique DetailSortie to an array', () => {
          const detailSortieArray: IDetailSortie[] = [{ id: 123 }, { id: 456 }, { id: 920 }];
          const detailSortieCollection: IDetailSortie[] = [{ id: 123 }];
          expectedResult = service.addDetailSortieToCollectionIfMissing(detailSortieCollection, ...detailSortieArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const detailSortie: IDetailSortie = { id: 123 };
          const detailSortie2: IDetailSortie = { id: 456 };
          expectedResult = service.addDetailSortieToCollectionIfMissing([], detailSortie, detailSortie2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(detailSortie);
          expect(expectedResult).toContain(detailSortie2);
        });

        it('should accept null and undefined values', () => {
          const detailSortie: IDetailSortie = { id: 123 };
          expectedResult = service.addDetailSortieToCollectionIfMissing([], null, detailSortie, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(detailSortie);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
