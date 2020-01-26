import { HttpParams } from '@angular/common/http';
import { PageParams } from './PageParams';

export class PageRequest {
  sortColumn: string;
  direction: string;
  currentPage = 1;
  resultsPerPage = 20;

  constructor(pageNumber: number, resultsPerPage: number = 20, sortColumn: string = 'id', direction: string = 'desc') {
    this.currentPage = pageNumber;
    this.resultsPerPage = resultsPerPage;
    this.sortColumn = sortColumn;
    this.direction = direction;
  }

  toHttpParams(): HttpParams {
    let params = new HttpParams();
    params = params.set(PageParams.SIZE, this.resultsPerPage.toString());
    params = params.set(PageParams.PAGE, this.currentPage.toString());
    params = params.set(PageParams.SORT, `${this.sortColumn},${this.direction}`);
    return params;
  }
}
