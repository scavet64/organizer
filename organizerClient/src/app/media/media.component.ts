import { COMMA, ENTER, TAB } from '@angular/cdk/keycodes';
import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { MediaFile } from './media.file';
import { TagModel } from '../tags/tagModel';
import { PaginationResponse } from '../common/page-response';
import { MediaService } from './media.service';
import { TagService } from '../tags/tag.service';
import { MatAutocomplete, MatChipInputEvent, MatAutocompleteSelectedEvent } from '@angular/material';
import { startWith, map, flatMap } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { FormControl } from '@angular/forms';
import { MediaSearchRequest } from './requests/media-search-request';
import { SlideInOut } from '../animations/SlideInOut';
import { MediaSortType } from './media.sort.type';
import { ActivatedRoute, Router } from '@angular/router';
import { VideoplayerService } from '../videoplayer/videoplayer.service';

@Component({
  selector: 'app-media',
  templateUrl: './media.component.html',
  styleUrls: ['./media.component.scss'],
  animations: [SlideInOut]
})
export class MediaComponent implements OnInit {
  // Default search properties
  public static readonly DEFAULT_PAGE = 0;
  public static readonly DEFAULT_RESULTS_PER_PAGE = 20;
  public static readonly DEFAULT_SORT_COLUMN = MediaSortType.DateAddedSort.value;
  public static readonly DEFAULT_SORT_DIRECTION = 'desc';
  public static readonly DEFAULT_ONLY_SHOW_FAVORITE = false;
  public static readonly DEFAULT_MEDIA_TYPE = null;

  // Private readonly properties specific for this component
  private readonly advanceSearchStateKey = 'mediaAdvanceSearch';
  readonly pageSizeOptions = [10, 20, 50];
  readonly separatorKeysCodes: number[] = [ENTER, COMMA, TAB];
  readonly mediaSortOptions: MediaSortType[] = MediaSortType.mediaSortOptions;

  // Visual based properties. These can change based on user interaction
  showAdvanceSearch = false;
  advanceSearchState = 'closed';
  isListView = false;

  // Tag related properties.
  tagControl = new FormControl();
  readonly selectable = true;
  readonly addOnBlur = true;            // True means that the tag will be added when the control loses focus
  knownTags: TagModel[];                // All known tags in the application
  filteredTags: Observable<TagModel[]>; // Filtered tags when typing in the list.

  // Search related properties.
  searchBox: string;
  folderName: string;
  selectedTags: TagModel[] = [];
  sortColumn: string;
  sortDirection: string;
  onlyShowFavorite: boolean;
  mediaFilter: string;

  // Media Type Controller
  selectedMediaIndex = 0;

  // The actual data
  pageResponse: PaginationResponse<MediaFile>;

  @ViewChild('chipInput', { static: false }) chipInput: ElementRef<HTMLInputElement>;
  @ViewChild('auto', { static: false }) matAutocomplete: MatAutocomplete;

  constructor(
    private activatedRoute: ActivatedRoute,
    private mediaFileService: MediaService,
    private router: Router,
    private tagService: TagService,
    private videoplayerService: VideoplayerService
  ) {
  }

  buildSearchRequest(
    page = MediaComponent.DEFAULT_PAGE,
    pageSize = MediaComponent.DEFAULT_RESULTS_PER_PAGE,
    tagIds = null
  ): MediaSearchRequest {
    if (!tagIds) {
      tagIds = [];
      this.selectedTags.forEach(tag => tagIds.push(tag.id));
    }

    return new MediaSearchRequest(
      this.searchBox,
      tagIds,
      this.sortColumn,
      this.sortDirection,
      this.mediaFilter,
      this.onlyShowFavorite,
      page,
      pageSize,
      this.folderName
    );
  }

  ngOnInit() {
    const storedPref = localStorage.getItem(this.advanceSearchStateKey);
    if (storedPref) {
      this.advanceSearchState = storedPref;
    }

    console.log("init running");
    this.tagService.getAllTags().pipe(
      flatMap((tagsResult) => {
        console.log("Tags:");
        this.knownTags = tagsResult.data;
        console.log(this.knownTags);
        this.filteredTags = this.tagControl.valueChanges.pipe(
          startWith(null),
          map((tagname: string | null) => {
            console.log('filtertag');
            console.log(tagname);
            if (typeof tagname === 'string') {
              return tagname ? this._filter(tagname) : this.knownTags.slice();
            }
          }));
        console.log(this.filteredTags);
        return this.activatedRoute.queryParams;
      }),
      flatMap((params) => {
        // Process tag list
        this.selectedTags = [];
        if (params.tagIds) {
          const tagIds = params.tagIds.split(',');
          tagIds.forEach(id => {
            this.selectedTags.push(this.getTagFromId(parseInt(id, 10)));
          });
        }

        console.log(params);
        this.searchBox = params.name;
        this.folderName = params.path;
        this.sortColumn = params.sortColumn || MediaComponent.DEFAULT_SORT_COLUMN;
        this.sortDirection = params.sortDirection || MediaComponent.DEFAULT_SORT_DIRECTION;
        this.mediaFilter = params.mediaType || MediaComponent.DEFAULT_MEDIA_TYPE;
        this.onlyShowFavorite = params.onlyShowFavorite || MediaComponent.DEFAULT_ONLY_SHOW_FAVORITE;
        const searchRequest = this.buildSearchRequest(params.currentPage, params.resultsPerPage, params.tagIds);
        console.log('Init Search Request:');
        console.log(searchRequest);
        this.selectedMediaIndex = this.mediaFilterToIndexValue(this.mediaFilter);
        return this.mediaFileService.getMediaPagesSearch(searchRequest);
      })
    ).subscribe((mediaSearchResult) => {
      console.log(mediaSearchResult);
      this.pageResponse = mediaSearchResult.data;
      console.log(this.pageResponse);
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
        const matchingTag = this.getTagFromName(value);
        if (matchingTag) {
          this.selectedTags.push(matchingTag);
        } else {

        }
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

  private getTagFromId(id: number) {
    console.log(this.knownTags);
    return this.knownTags.find(tag => tag.id === id);
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

  onPageChange(event) {
    this.search(event.pageIndex, event.pageSize);
  }

  search(pageNum = 0, pageSize = this.pageResponse ? this.pageResponse.size : 20) {
    const request = this.buildSearchRequest(pageNum, pageSize);

    this.router.navigate(['/media'], {
      queryParams: request.toUrlParams()
    });
  }

  tagClicked(event: any) {
    this.selectedTags.push(event);
    this.search();
  }

  // TODO: Look into moving into media-list.component
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

  // TODO: Look into moving into media-list.component
  mediaFilterToIndexValue(value) {
    switch (value) {
      case 'video':
        return 1;
      case 'image':
        return 2;
      default:
        return 0;
    }
  }

  showMoreFilters() {
    this.advanceSearchState = this.advanceSearchState === 'closed' ? 'opened' : 'closed';
    localStorage.setItem(this.advanceSearchStateKey, this.advanceSearchState);
  }

  playRandomVideo() {
    this.mediaFileService.getRandomVideo().subscribe(res => {
      console.log(`random video - ${res.data.name}`)
      this.videoplayerService.showVideo(res.data, false);
    });
  }
}
