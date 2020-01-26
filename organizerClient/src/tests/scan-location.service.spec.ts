import { TestBed } from '@angular/core/testing';

import { ScanLocationService } from '../app/scan-locations/scan-location.service';

describe('ScanLocationService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ScanLocationService = TestBed.get(ScanLocationService);
    expect(service).toBeTruthy();
  });
});
