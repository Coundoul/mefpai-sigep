import { ComponentFixture, TestBed } from '@angular/core/testing';

import { QuartierInfraComponent } from './quartier-infra.component';

describe('QuartierInfraComponent', () => {
  let component: QuartierInfraComponent;
  let fixture: ComponentFixture<QuartierInfraComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ QuartierInfraComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(QuartierInfraComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
