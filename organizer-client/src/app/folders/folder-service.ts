import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Response } from '../response';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Folder } from './folder';
import { environment } from 'src/environments/environment';
import { timeout } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class FolderService {

  constructor(
    private http: HttpClient
  ) { }

  public getRootFolder(): Observable<Response<any>> {
    console.log(environment.baseURL);
    return this.http
    .get<Response<any>>(`${environment.baseURL}folder/root`)
    .pipe(timeout(10000));
  }
}
