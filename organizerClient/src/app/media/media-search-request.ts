import { HttpParams } from '@angular/common/http';
import { PageRequest } from '../common/PageRequest';
import { TagModel } from '../tags/tagModel';

export class MediaSearchRequest extends PageRequest {
  name: string;
  tagIds: number[];

  constructor(pageNumber: number, name: string, tags: TagModel[]) {
    super(pageNumber);
    this.name = name;
    this.tagIds = new Array();

    if (tags != null) {
      tags.forEach(tag => this.tagIds.push(tag.id));
    }

  }

  toHttpParams(): HttpParams {
    let params = super.toHttpParams();
    if (this.name && this.name !== '') {
      params = params.set('name', `like:${this.name}`);
    }

    if (this.tagIds.length > 0) {
      params = params.set('tags', this.tagIds.toString());
    }
    return params;
  }
}
