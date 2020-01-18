import { HttpParams } from '@angular/common/http';
import { PageRequest } from '../common/PageRequest';
import { TagModel } from '../tags/tagModel';
import { PageParams } from '../common/PageParams';

export class MediaSearchRequest extends PageRequest {
  sortColumn: string;
  sortDirection: string;
  name: string;
  tagIds: number[];

  constructor(pageNumber: number, name: string, tags: TagModel[], sortColumn: string = 'id', sortDirection: string = 'desc') {
    super(pageNumber);
    this.name = name;
    this.tagIds = new Array();
    this.sortColumn = sortColumn;
    this.sortDirection = sortDirection;

    if (tags != null) {
      tags.forEach(tag => this.tagIds.push(tag.id));
    }

  }

  toHttpParams(): HttpParams {
    let params = super.toHttpParams();
    if (this.name && this.name !== '') {
      params = params.set('name', `like:${this.name}`.toLowerCase());
    }

    if (this.tagIds.length > 0) {
      params = params.set('tags', this.tagIds.toString());
    }

    if (this.sortColumn && this.sortDirection) {
      params = params.set(PageParams.SORT, `${this.sortColumn},${this.sortDirection}`);
    }
    return params;
  }
}
