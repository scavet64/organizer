import { HttpParams } from '@angular/common/http';
import { PageRequest } from '../../common/PageRequest';
import { PageParams } from '../../common/PageParams';

export class FolderMediaSearchRequest extends PageRequest {

  folderId;

  constructor(
    folderId: string,
    sortColumn: string,
    sortDirection: string,
    pageNumber: number,
    resultsPerPage: number
    ) {
    super(pageNumber, resultsPerPage);
    this.folderId = folderId;
    this.sortColumn = sortColumn;
    this.direction = sortDirection;
  }

  toUrlParams() {
    return {
      ...((this.folderId && this.folderId !== '') && {name: this.folderId}),
      ...(this.sortColumn !== 'dateAdded' && { sortColumn: this.sortColumn }),
      ...(this.direction !== 'desc' && { sortDirection: this.direction}),
      ...(this.currentPage !== 0 && { currentPage: this.currentPage }),
      ...(this.resultsPerPage !== 20 && {resultsPerPage: this.resultsPerPage})
    };
  }

  toHttpParams(): HttpParams {
    let params = super.toHttpParams();
    if (this.folderId && this.folderId !== '') {
      params = params.set('folderId', this.folderId);
    }

    if (this.sortColumn && this.direction) {
      params = params.set(PageParams.SORT, `${this.sortColumn},${this.direction}`);
    }
    return params;
  }
}
