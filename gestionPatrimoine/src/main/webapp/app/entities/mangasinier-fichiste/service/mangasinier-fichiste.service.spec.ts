import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Sexe } from 'app/entities/enumerations/sexe.model';
import { Direction } from 'app/entities/enumerations/direction.model';
import { IMangasinierFichiste, MangasinierFichiste } from '../mangasinier-fichiste.model';

import { MangasinierFichisteService } from './mangasinier-fichiste.service';

describe('Service Tests', () => {
  describe('MangasinierFichiste Service', () => {
    let service: MangasinierFichisteService;
    let httpMock: HttpTestingController;
    let elemDefault: IMangasinierFichiste;
    let expectedResult: IMangasinierFichiste | IMangasinierFichiste[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(MangasinierFichisteService);
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

      it('should create a MangasinierFichiste', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new MangasinierFichiste()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a MangasinierFichiste', () => {
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

      it('should partial update a MangasinierFichiste', () => {
        const patchObject = Object.assign(
          {
            nomPers: 'BBBBBB',
            prenomPers: 'BBBBBB',
            mobile: 'BBBBBB',
            direction: 'BBBBBB',
          },
          new MangasinierFichiste()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of MangasinierFichiste', () => {
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

      it('should delete a MangasinierFichiste', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addMangasinierFichisteToCollectionIfMissing', () => {
        it('should add a MangasinierFichiste to an empty array', () => {
          const mangasinierFichiste: IMangasinierFichiste = { id: 123 };
          expectedResult = service.addMangasinierFichisteToCollectionIfMissing([], mangasinierFichiste);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(mangasinierFichiste);
        });

        it('should not add a MangasinierFichiste to an array that contains it', () => {
          const mangasinierFichiste: IMangasinierFichiste = { id: 123 };
          const mangasinierFichisteCollection: IMangasinierFichiste[] = [
            {
              ...mangasinierFichiste,
            },
            { id: 456 },
          ];
          expectedResult = service.addMangasinierFichisteToCollectionIfMissing(mangasinierFichisteCollection, mangasinierFichiste);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a MangasinierFichiste to an array that doesn't contain it", () => {
          const mangasinierFichiste: IMangasinierFichiste = { id: 123 };
          const mangasinierFichisteCollection: IMangasinierFichiste[] = [{ id: 456 }];
          expectedResult = service.addMangasinierFichisteToCollectionIfMissing(mangasinierFichisteCollection, mangasinierFichiste);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(mangasinierFichiste);
        });

        it('should add only unique MangasinierFichiste to an array', () => {
          const mangasinierFichisteArray: IMangasinierFichiste[] = [{ id: 123 }, { id: 456 }, { id: 30343 }];
          const mangasinierFichisteCollection: IMangasinierFichiste[] = [{ id: 123 }];
          expectedResult = service.addMangasinierFichisteToCollectionIfMissing(mangasinierFichisteCollection, ...mangasinierFichisteArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const mangasinierFichiste: IMangasinierFichiste = { id: 123 };
          const mangasinierFichiste2: IMangasinierFichiste = { id: 456 };
          expectedResult = service.addMangasinierFichisteToCollectionIfMissing([], mangasinierFichiste, mangasinierFichiste2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(mangasinierFichiste);
          expect(expectedResult).toContain(mangasinierFichiste2);
        });

        it('should accept null and undefined values', () => {
          const mangasinierFichiste: IMangasinierFichiste = { id: 123 };
          expectedResult = service.addMangasinierFichisteToCollectionIfMissing([], null, mangasinierFichiste, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(mangasinierFichiste);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
