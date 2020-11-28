import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { timeout } from 'rxjs/operators';
import { Response } from '../common/response';
import { PageRequest } from '../common/PageRequest';

@Injectable({
  providedIn: 'root'
})
export class FolderService {

  constructor(
    private http: HttpClient
  ) { }

  public getRootFolders(): Observable<Response<any>> {
    return this.http
      .get<Response<any>>(`${environment.baseURL}folder/root`)
      .pipe(timeout(10000));
  }

  public getFolder(id: number): Observable<Response<any>> {
    return this.http
      .get<Response<any>>(`${environment.baseURL}folder?folderId=${id}`)
      .pipe(timeout(10000));
  }

  public getFolderPage(id: number, pageRequest: PageRequest): Observable<Response<any>> {
    const data = pageRequest.toHttpParams();
    return this.http
      .get<Response<any>>(`${environment.baseURL}folder/media?folderId=${id}`, { params: data })
      .pipe(timeout(10000));
  }
}
