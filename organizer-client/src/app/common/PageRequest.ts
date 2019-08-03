import { HttpParams } from '@angular/common/http';
import { PageParams } from './PageParams';

export class PageRequest {
    // sort: SortTypes;
    currentPage = 1;
    resultsPerPage = 20;

    constructor(pageNumber: number) {
        this.currentPage = pageNumber;
    }

    toHttpParams(): HttpParams {
        let params = new HttpParams();
        // params = params.set(PageParams.SORT, `${this.sort},desc`);
        params = params.set(PageParams.SIZE, this.resultsPerPage.toString());
        params = params.set(PageParams.PAGE, this.currentPage.toString());
        return params;
    }
}
