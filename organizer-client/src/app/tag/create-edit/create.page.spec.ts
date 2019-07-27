import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateEditPage } from './create-edit.page';

describe('CreatePage', () => {
  let component: CreateEditPage;
  let fixture: ComponentFixture<CreateEditPage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CreateEditPage ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateEditPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
