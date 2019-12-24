import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ScanLocationsComponent } from '../app/scan-locations/scan-locations.component';

describe('ScanLocationsComponent', () => {
  let component: ScanLocationsComponent;
  let fixture: ComponentFixture<ScanLocationsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ScanLocationsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ScanLocationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
