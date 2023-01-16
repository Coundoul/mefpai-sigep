import { TestBed } from '@angular/core/testing';

import { MatieresMaintenancesService } from './matieres-maintenances.service';

describe('MatieresMaintenancesService', () => {
  let service: MatieresMaintenancesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MatieresMaintenancesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
