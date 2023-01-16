import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EtatinfraComponent } from './etatinfra.component';

describe('EtatinfraComponent', () => {
  let component: EtatinfraComponent;
  let fixture: ComponentFixture<EtatinfraComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EtatinfraComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EtatinfraComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
