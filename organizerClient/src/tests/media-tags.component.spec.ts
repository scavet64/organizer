import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MediaTagsComponent } from '../app/media/media-tags/media-tags.component';

describe('MediaTagsComponent', () => {
  let component: MediaTagsComponent;
  let fixture: ComponentFixture<MediaTagsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MediaTagsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MediaTagsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
