import { TestBed } from '@angular/core/testing';

import { ToastingService } from './toasting.service';

describe('ToastingService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ToastingService = TestBed.get(ToastingService);
    expect(service).toBeTruthy();
  });
});
