import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MatieresMaintenancesComponent } from './matieres-maintenances.component';

describe('MatieresMaintenancesComponent', () => {
  let component: MatieresMaintenancesComponent;
  let fixture: ComponentFixture<MatieresMaintenancesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MatieresMaintenancesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MatieresMaintenancesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
