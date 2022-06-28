import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IUtilisateurFinal, UtilisateurFinal } from '../utilisateur-final.model';

import { UtilisateurFinalService } from './utilisateur-final.service';

describe('Service Tests', () => {
  describe('UtilisateurFinal Service', () => {
    let service: UtilisateurFinalService;
    let httpMock: HttpTestingController;
    let elemDefault: IUtilisateurFinal;
    let expectedResult: IUtilisateurFinal | IUtilisateurFinal[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(UtilisateurFinalService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nomUtilisateur: 'AAAAAAA',
        prenomUtilisateur: 'AAAAAAA',
        emailInstitutionnel: 'AAAAAAA',
        mobile: 'AAAAAAA',
        sexe: 'AAAAAAA',
        departement: 'AAAAAAA',
        serviceDep: 'AAAAAAA',
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

      it('should create a UtilisateurFinal', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new UtilisateurFinal()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a UtilisateurFinal', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nomUtilisateur: 'BBBBBB',
            prenomUtilisateur: 'BBBBBB',
            emailInstitutionnel: 'BBBBBB',
            mobile: 'BBBBBB',
            sexe: 'BBBBBB',
            departement: 'BBBBBB',
            serviceDep: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a UtilisateurFinal', () => {
        const patchObject = Object.assign(
          {
            emailInstitutionnel: 'BBBBBB',
            mobile: 'BBBBBB',
            sexe: 'BBBBBB',
            departement: 'BBBBBB',
          },
          new UtilisateurFinal()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of UtilisateurFinal', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nomUtilisateur: 'BBBBBB',
            prenomUtilisateur: 'BBBBBB',
            emailInstitutionnel: 'BBBBBB',
            mobile: 'BBBBBB',
            sexe: 'BBBBBB',
            departement: 'BBBBBB',
            serviceDep: 'BBBBBB',
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

      it('should delete a UtilisateurFinal', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addUtilisateurFinalToCollectionIfMissing', () => {
        it('should add a UtilisateurFinal to an empty array', () => {
          const utilisateurFinal: IUtilisateurFinal = { id: 123 };
          expectedResult = service.addUtilisateurFinalToCollectionIfMissing([], utilisateurFinal);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(utilisateurFinal);
        });

        it('should not add a UtilisateurFinal to an array that contains it', () => {
          const utilisateurFinal: IUtilisateurFinal = { id: 123 };
          const utilisateurFinalCollection: IUtilisateurFinal[] = [
            {
              ...utilisateurFinal,
            },
            { id: 456 },
          ];
          expectedResult = service.addUtilisateurFinalToCollectionIfMissing(utilisateurFinalCollection, utilisateurFinal);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a UtilisateurFinal to an array that doesn't contain it", () => {
          const utilisateurFinal: IUtilisateurFinal = { id: 123 };
          const utilisateurFinalCollection: IUtilisateurFinal[] = [{ id: 456 }];
          expectedResult = service.addUtilisateurFinalToCollectionIfMissing(utilisateurFinalCollection, utilisateurFinal);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(utilisateurFinal);
        });

        it('should add only unique UtilisateurFinal to an array', () => {
          const utilisateurFinalArray: IUtilisateurFinal[] = [{ id: 123 }, { id: 456 }, { id: 49904 }];
          const utilisateurFinalCollection: IUtilisateurFinal[] = [{ id: 123 }];
          expectedResult = service.addUtilisateurFinalToCollectionIfMissing(utilisateurFinalCollection, ...utilisateurFinalArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const utilisateurFinal: IUtilisateurFinal = { id: 123 };
          const utilisateurFinal2: IUtilisateurFinal = { id: 456 };
          expectedResult = service.addUtilisateurFinalToCollectionIfMissing([], utilisateurFinal, utilisateurFinal2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(utilisateurFinal);
          expect(expectedResult).toContain(utilisateurFinal2);
        });

        it('should accept null and undefined values', () => {
          const utilisateurFinal: IUtilisateurFinal = { id: 123 };
          expectedResult = service.addUtilisateurFinalToCollectionIfMissing([], null, utilisateurFinal, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(utilisateurFinal);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
