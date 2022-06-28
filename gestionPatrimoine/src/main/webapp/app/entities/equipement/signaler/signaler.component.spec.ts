/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { SignalerComponent } from './signaler.component';

describe('SignalerComponent', () => {
  let component: SignalerComponent;
  let fixture: ComponentFixture<SignalerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [SignalerComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SignalerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
