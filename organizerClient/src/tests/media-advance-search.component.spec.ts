import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MediaAdvanceSearchComponent } from '../app/media/media-advance-search/media-advance-search.component';

describe('MediaAdvanceSearchComponent', () => {
  let component: MediaAdvanceSearchComponent;
  let fixture: ComponentFixture<MediaAdvanceSearchComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MediaAdvanceSearchComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MediaAdvanceSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
