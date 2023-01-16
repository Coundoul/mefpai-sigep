import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MatiereCategorieComponent } from './matiere-categorie.component';

describe('MatiereCategorieComponent', () => {
  let component: MatiereCategorieComponent;
  let fixture: ComponentFixture<MatiereCategorieComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MatiereCategorieComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MatiereCategorieComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
