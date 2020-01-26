import { TestBed } from '@angular/core/testing';

import { BrowseFileSystemService } from './browse-file-system.service';

describe('BrowseFileSystemService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: BrowseFileSystemService = TestBed.get(BrowseFileSystemService);
    expect(service).toBeTruthy();
  });
});
