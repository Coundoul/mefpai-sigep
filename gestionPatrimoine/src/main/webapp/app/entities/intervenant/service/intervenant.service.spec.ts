import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { TypeMaitre } from 'app/entities/enumerations/type-maitre.model';
import { IIntervenant, Intervenant } from '../intervenant.model';

import { IntervenantService } from './intervenant.service';

describe('Service Tests', () => {
  describe('Intervenant Service', () => {
    let service: IntervenantService;
    let httpMock: HttpTestingController;
    let elemDefault: IIntervenant;
    let expectedResult: IIntervenant | IIntervenant[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(IntervenantService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nomIntervenant: 'AAAAAAA',
        prenomIntervenant: 'AAAAAAA',
        emailProfessionnel: 'AAAAAAA',
        raisonSocial: 'AAAAAAA',
        maitre: TypeMaitre.MaitreOuvrage,
        role: 'AAAAAAA',
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

      it('should create a Intervenant', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Intervenant()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Intervenant', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nomIntervenant: 'BBBBBB',
            prenomIntervenant: 'BBBBBB',
            emailProfessionnel: 'BBBBBB',
            raisonSocial: 'BBBBBB',
            maitre: 'BBBBBB',
            role: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Intervenant', () => {
        const patchObject = Object.assign({}, new Intervenant());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Intervenant', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nomIntervenant: 'BBBBBB',
            prenomIntervenant: 'BBBBBB',
            emailProfessionnel: 'BBBBBB',
            raisonSocial: 'BBBBBB',
            maitre: 'BBBBBB',
            role: 'BBBBBB',
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

      it('should delete a Intervenant', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addIntervenantToCollectionIfMissing', () => {
        it('should add a Intervenant to an empty array', () => {
          const intervenant: IIntervenant = { id: 123 };
          expectedResult = service.addIntervenantToCollectionIfMissing([], intervenant);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(intervenant);
        });

        it('should not add a Intervenant to an array that contains it', () => {
          const intervenant: IIntervenant = { id: 123 };
          const intervenantCollection: IIntervenant[] = [
            {
              ...intervenant,
            },
            { id: 456 },
          ];
          expectedResult = service.addIntervenantToCollectionIfMissing(intervenantCollection, intervenant);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Intervenant to an array that doesn't contain it", () => {
          const intervenant: IIntervenant = { id: 123 };
          const intervenantCollection: IIntervenant[] = [{ id: 456 }];
          expectedResult = service.addIntervenantToCollectionIfMissing(intervenantCollection, intervenant);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(intervenant);
        });

        it('should add only unique Intervenant to an array', () => {
          const intervenantArray: IIntervenant[] = [{ id: 123 }, { id: 456 }, { id: 32162 }];
          const intervenantCollection: IIntervenant[] = [{ id: 123 }];
          expectedResult = service.addIntervenantToCollectionIfMissing(intervenantCollection, ...intervenantArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const intervenant: IIntervenant = { id: 123 };
          const intervenant2: IIntervenant = { id: 456 };
          expectedResult = service.addIntervenantToCollectionIfMissing([], intervenant, intervenant2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(intervenant);
          expect(expectedResult).toContain(intervenant2);
        });

        it('should accept null and undefined values', () => {
          const intervenant: IIntervenant = { id: 123 };
          expectedResult = service.addIntervenantToCollectionIfMissing([], null, intervenant, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(intervenant);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
