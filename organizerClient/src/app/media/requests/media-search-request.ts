import { HttpParams } from '@angular/common/http';
import { PageRequest } from '../../common/PageRequest';
import { PageParams } from '../../common/PageParams';

export class MediaSearchRequest extends PageRequest {

  sortColumn: string;
  sortDirection: string;
  name: string;
  tagIds: number[];
  mediaType: string;
  onlyShowFavorite: boolean;
  showIgnored: boolean;
  folderName: string;

  constructor(
    name: string,
    tags: number[],
    sortColumn: string,
    sortDirection: string,
    mediaType: string,
    onlyShowFavorite: boolean,
    pageNumber: number,
    resultsPerPage: number,
    folderName: string
    ) {
    super(pageNumber, resultsPerPage);
    this.name = name;
    this.tagIds = new Array();
    this.sortColumn = sortColumn;
    this.sortDirection = sortDirection;
    this.mediaType = mediaType;
    this.onlyShowFavorite = onlyShowFavorite;
    this.tagIds = tags;
    this.showIgnored = false;
    this.folderName = folderName;
  }

  toUrlParams() {
    return {
      ...((this.name && this.name !== '') && {name: this.name}),
      ...((this.folderName && this.folderName !== '') && {path: this.folderName}),
      ...((this.mediaType && this.mediaType !== '') && {mediaType: this.mediaType}),
      ...(this.tagIds.length > 0 && {tagIds: this.tagIds.toString()}),
      ...(this.sortColumn !== 'dateAdded' && { sortColumn: this.sortColumn }),
      ...(this.sortDirection !== 'desc' && { sortDirection: this.sortDirection}),
      ...(this.onlyShowFavorite && { onlyShowFavorite: this.onlyShowFavorite }),
      ...(this.showIgnored && { showIgnored: this.showIgnored }),
      ...(this.currentPage !== 0 && { currentPage: this.currentPage }),
      ...(this.resultsPerPage !== 20 && {resultsPerPage: this.resultsPerPage})
    };
  }

  toHttpParams(): HttpParams {
    let params = super.toHttpParams();
    if (this.name && this.name !== '') {
      params = params.set('name', `like:${this.name}`.toLowerCase());
    }

    if (this.folderName && this.folderName !== '') {
      params = params.set('path', `like:${this.folderName}`.toLowerCase());
    }

    if (this.mediaType && this.mediaType !== '') {
      params = params.set('mediaType', `like:${this.mediaType}`.toLowerCase());
    }

    if (this.tagIds.length > 0) {
      params = params.set('tags', this.tagIds.toString());
    }

    if (this.sortColumn && this.sortDirection) {
      params = params.set(PageParams.SORT, `${this.sortColumn},${this.sortDirection}`);
    }

    params = params.set(`isFavorite`, this.onlyShowFavorite.toString());

    params = params.set(`showIgnored`, this.showIgnored.toString());
    return params;
  }
}
