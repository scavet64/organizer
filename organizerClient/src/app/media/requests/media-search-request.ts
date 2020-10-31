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

  constructor(
    name: string,
    tags: number[],
    sortColumn: string,
    sortDirection: string,
    mediaType: string,
    onlyShowFavorite: boolean,
    pageNumber: number,
    resultsPerPage: number
    ) {
    super(pageNumber, resultsPerPage);
    this.name = name;
    this.tagIds = new Array();
    this.sortColumn = sortColumn;
    this.sortDirection = sortDirection;
    this.mediaType = mediaType;
    this.onlyShowFavorite = onlyShowFavorite;
    this.tagIds = tags;
  }

  toUrlParams() {
    return {
      ...((this.name && this.name !== '') && {name: this.name}),
      ...((this.mediaType && this.mediaType !== '') && {mediaType: this.mediaType}),
      ...(this.tagIds.length > 0 && {tagIds: this.tagIds.toString()}),
      ...(this.sortColumn !== 'dateAdded' && { sortColumn: this.sortColumn }),
      ...(this.sortDirection !== 'desc' && { sortDirection: this.sortDirection}),
      ...(this.onlyShowFavorite && { onlyShowFavorite: this.onlyShowFavorite }),
      ...(this.currentPage !== 0 && { currentPage: this.currentPage }),
      ...(this.resultsPerPage !== 20 && {resultsPerPage: this.resultsPerPage})
    };
  }

  toHttpParams(): HttpParams {
    let params = super.toHttpParams();
    if (this.name && this.name !== '') {
      params = params.set('name', `like:${this.name}`.toLowerCase());
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
    return params;
  }
}
