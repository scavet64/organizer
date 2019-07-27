import { Injectable } from '@angular/core';

import { environment } from 'src/environments/environment';
import { TagModel } from './tagModel';
import { Observable } from 'rxjs';
import { timeout } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';
import { Response } from '../response';

@Injectable({
  providedIn: 'root'
})
export class TagService {

  constructor(
    private http: HttpClient
  ) { }

  public getAllTags(): Observable<Response<any>> {
    return this.http
    .get<Response<any>>(`${environment.baseURL}tag/list`)
    .pipe(timeout(10000));
  }

  public deleteTag(id: number): Observable<Response<any>> {
    return this.http
    .delete<Response<any>>(`${environment.baseURL}tag?id=${id}`)
    .pipe(timeout(10000));
  }

  public createNewTag(tag: TagModel): Observable<Response<any>>  {
    return this.http
    .post<Response<any>>(`${environment.baseURL}tag/create`, tag)
    .pipe(timeout(10000));
  }
}
