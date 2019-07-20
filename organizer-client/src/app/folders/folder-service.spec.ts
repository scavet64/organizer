import { TestBed } from '@angular/core/testing';

import { FolderServiceService } from './folder-service';

describe('FolderServiceService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: FolderServiceService = TestBed.get(FolderServiceService);
    expect(service).toBeTruthy();
  });
});
