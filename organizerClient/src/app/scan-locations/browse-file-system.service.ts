import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { timeout } from 'rxjs/operators';
import { Response } from '../common/response';
import { BrowseRequest } from './browse-request';

@Injectable({
  providedIn: 'root'
})
export class BrowseFileSystemService {

  constructor(
    private http: HttpClient
  ) { }

  public getRootDirectories(): Observable<Response<any>> {
    return this.http
      .get<Response<any>>(`${environment.baseURL}browse/root`)
      .pipe(timeout(10000));
  }

  public getChildDirectories(path: string, showHidden: boolean): Observable<Response<any>> {
    const data = new BrowseRequest(path, showHidden);

    return this.http
      .post<Response<any>>(`${environment.baseURL}browse`, data)
      .pipe(timeout(10000));
  }
}
