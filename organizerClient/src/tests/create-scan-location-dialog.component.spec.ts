import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateScanLocationDialogComponent } from '../app/scan-locations/create-scan-location-dialog/create-scan-location-dialog.component';

describe('CreateScanLocationDialogComponent', () => {
  let component: CreateScanLocationDialogComponent;
  let fixture: ComponentFixture<CreateScanLocationDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CreateScanLocationDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateScanLocationDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
