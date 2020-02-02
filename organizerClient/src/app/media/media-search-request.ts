import { HttpParams } from '@angular/common/http';
import { PageRequest } from '../common/PageRequest';
import { TagModel } from '../tags/tagModel';
import { PageParams } from '../common/PageParams';

export class MediaSearchRequest extends PageRequest {
  sortColumn: string;
  sortDirection: string;
  name: string;
  tagIds: number[];
  mediaType: string;
  onlyShowFavorite: boolean;

  constructor(
    name: string,
    tags: TagModel[],
    sortColumn: string = 'id',
    sortDirection: string = 'desc',
    mediaType: string,
    onlyShowFavorite: boolean,
    pageNumber: number = 0,
    resultsPerPage: number = 20
    ) {
    super(pageNumber, resultsPerPage);
    this.name = name;
    this.tagIds = new Array();
    this.sortColumn = sortColumn;
    this.sortDirection = sortDirection;
    this.mediaType = mediaType;
    this.onlyShowFavorite = onlyShowFavorite;

    if (tags != null) {
      tags.forEach(tag => this.tagIds.push(tag.id));
    }

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
