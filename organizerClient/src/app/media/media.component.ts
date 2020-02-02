import { COMMA, ENTER } from '@angular/cdk/keycodes';
import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { MediaFile } from './media.file';
import { TagModel } from '../tags/tagModel';
import { PaginationResponse } from '../common/page-response';
import { MediaService } from './media.service';
import { TagService } from '../tags/tag.service';
import { MatAutocomplete, MatChipInputEvent, MatAutocompleteSelectedEvent } from '@angular/material';
import { startWith, map, filter } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { FormControl } from '@angular/forms';
import { AlertService } from '../alert/alert.service';
import { ResourceService } from './resource.service';
import { VideoplayerService } from '../videoplayer/videoplayer.service';
import { MediaSearchRequest } from './media-search-request';
import { SlideInOut } from '../animations/SlideInOut';
import { MediaSortType } from './media.sort.type';

@Component({
  selector: 'app-media',
  templateUrl: './media.component.html',
  styleUrls: ['./media.component.scss'],
  animations: [SlideInOut]
})
export class MediaComponent implements OnInit {
  private readonly advanceSearchStateKey = 'mediaAdvanceSearch';

  visible = true;
  selectable = true;
  removable = true;
  addOnBlur = true;
  separatorKeysCodes: number[] = [ENTER, COMMA];
  readonly pageSizeOptions = [10, 20, 50];
  pageResponse: PaginationResponse<MediaFile>;
  knownTags: TagModel[];
  isListView = true;
  searchBox: string;

  tagControl = new FormControl();

  selectedTags: TagModel[] = [];
  filteredTags: Observable<TagModel[]>;

  mediaSortOptions: MediaSortType[] = MediaSortType.mediaSortOptions;
  sortColumn: string = MediaSortType.DateAddedSort.value;
  sortDirection: string = 'desc';
  onlyShowFavorite = false;
  mediaFilter: string = null;

  selected = new FormControl(0);

  showAdvanceSearch = false;
  advanceSearchState = 'closed';

  @ViewChild('chipInput', { static: false }) chipInput: ElementRef<HTMLInputElement>;
  @ViewChild('auto', { static: false }) matAutocomplete: MatAutocomplete;

  constructor(
    private alertService: AlertService,
    private mediaFileService: MediaService,
    private resourceService: ResourceService,
    private tagService: TagService,
    public videoplayerService: VideoplayerService
  ) {
  }

  ngOnInit() {
    const storedPref = localStorage.getItem(this.advanceSearchStateKey);
    if (storedPref) {
      this.advanceSearchState = storedPref;
    }
    this.search();

    this.tagService.getAllTags().subscribe(res => {
      this.knownTags = res.data;
      console.log(this.knownTags);
      this.filteredTags = this.tagControl.valueChanges.pipe(
        startWith(null),
        map((tagname: string | null) => tagname ? this._filter(tagname) : this.knownTags.slice()));
    }, (err) => {
      console.log(`Could not get tags`);
    });
  }

  onPageChange(event) {
    console.log(event);
    console.log(event.pageIndex);
    const request = new MediaSearchRequest(
      this.searchBox, this.selectedTags, this.sortColumn, this.sortDirection, this.mediaFilter, event.pageIndex, event.pageSize
    );
    this.mediaFileService.getMediaPagesSearch(request).subscribe(res => {
      this.pageResponse = res.data;
    });
  }

  addTagToSearch(event: MatChipInputEvent): void {
    // Add tag only when MatAutocomplete is not open
    // To make sure this does not conflict with OptionSelected Event
    if (!this.matAutocomplete.isOpen) {
      const input = event.input;
      const value = event.value;

      // Add our Tag
      if ((value || '').trim()) {
        this.selectedTags.push(this.getTagFromName(value));
        // Kick off a search
        this.search();
      }

      // Reset the input value
      if (input) {
        input.value = '';
      }

      this.tagControl.setValue(null);
    }
  }

  private getTagFromName(name: string) {
    return this.knownTags.find(tag => tag.name === name);
  }

  removeTagFromSearch(tag: TagModel): void {
    const index = this.selectedTags.indexOf(tag);

    if (index >= 0) {
      this.selectedTags.splice(index, 1);
      this.search();
    }
  }

  selectedTag(event: MatAutocompleteSelectedEvent): void {
    this.selectedTags.push(this.getTagFromName(event.option.viewValue));
    this.chipInput.nativeElement.value = '';
    this.tagControl.setValue(null);
    this.search();
  }

  private _filter(tagName: string): TagModel[] {
    const filterValue = tagName.toLowerCase();

    const filtered = this.knownTags.filter(tag => tag.name.toLowerCase().indexOf(filterValue) === 0);
    console.log(filtered);
    return filtered;
  }

  search() {
    console.log(this.searchBox);
    console.log(this.sortColumn);
    console.log(this.sortDirection);
    console.log(this.onlyShowFavorite);
    const request = new MediaSearchRequest(
      this.searchBox,
      this.selectedTags,
      this.sortColumn,
      this.sortDirection,
      this.mediaFilter,
      this.onlyShowFavorite,
      0,
      this.pageResponse ? this.pageResponse.size : 20
    );
    this.mediaFileService.getMediaPagesSearch(request).subscribe(res => {
      this.pageResponse = res.data;
      console.log(this.pageResponse);
    });
  }

  updateMediaFilesTags(event: any) {
    this.mediaFileService.updateMediaFileTags(event.file.id, event.editedTags).subscribe(result => {
      event.file.tags = result.data.tags;
      this.alertService.success(`Successfully edited tags for file`);
    }, error => {
      this.alertService.error(`Failed to edit tags: ${error.error.error}`);
    });
  }

  getThumbnailSrc(mediaFile: MediaFile): string {
    let url;
    if (mediaFile.mimetype.includes('video')) {
      // This is a video so use a thumbnail if it has one.
      if (mediaFile.thumbnail) {
        url = this.resourceService.getThumbnailUrl(mediaFile.thumbnail);
      }
    } else if (mediaFile.mimetype.includes('image')) {
      url = this.resourceService.getMediaUrl(mediaFile);
    }

    // if URL couldn't be generated, fall back to this.
    if (!url) {
      url = 'https://i.kym-cdn.com/photos/images/newsfeed/001/460/439/32f.jpg';
    }
    return url;
  }

  tagClicked(event: any) {
    this.selectedTags.push(event);
    this.search();
  }

  openMedia(file: MediaFile) {
    this.videoplayerService.showVideo(file);
  }

  mediaTypeFilterChanged(event) {
    console.log(event);
    switch (event) {
      case 0:
        this.mediaFilter = null;
        break;
      case 1:
        this.mediaFilter = 'video';
        break;
      case 2:
        this.mediaFilter = 'image';
        break;
    }
    this.search();
  }

  showMoreFilters() {
    this.advanceSearchState = this.advanceSearchState === 'closed' ? 'opened' : 'closed';
    localStorage.setItem(this.advanceSearchStateKey, this.advanceSearchState);
  }

  favoriteToggle(mediaFile: MediaFile) {
    this.mediaFileService.toggleFavorite(mediaFile.id, !mediaFile.isFavorite).subscribe(res => {
      mediaFile.isFavorite = res.data.isFavorite;
      this.alertService.success(`Successfully favorited media`);
    });
  }
}
