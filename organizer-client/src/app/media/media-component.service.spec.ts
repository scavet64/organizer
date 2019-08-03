import { TestBed } from '@angular/core/testing';

import { MediaComponentService } from './media-component.service';

describe('MediaComponentService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: MediaComponentService = TestBed.get(MediaComponentService);
    expect(service).toBeTruthy();
  });
});
