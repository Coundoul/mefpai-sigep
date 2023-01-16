import { TestBed } from '@angular/core/testing';
import { InfrastructuresMatieresService } from './infrastructures-matieres.service';



describe('InfrastructuresMatieresService', () => {
  let service: InfrastructuresMatieresService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InfrastructuresMatieresService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
