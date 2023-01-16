import { ComponentFixture, TestBed } from '@angular/core/testing';



describe('InfrastructuresMaintenancesComponent', () => {
  let component: InfrastructuresMaintenancesComponent;
  let fixture: ComponentFixture<InfrastructuresMaintenancesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InfrastructuresMaintenancesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InfrastructuresMaintenancesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
