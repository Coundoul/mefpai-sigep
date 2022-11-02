/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { ListProjetsAllComponent } from './list-projets-all.component';

describe('ListProjetsAllComponent', () => {
  let component: ListProjetsAllComponent;
  let fixture: ComponentFixture<ListProjetsAllComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ListProjetsAllComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ListProjetsAllComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
