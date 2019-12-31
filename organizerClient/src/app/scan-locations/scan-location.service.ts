import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { timeout } from 'rxjs/operators';
import { Response } from '../common/response';
import { ScanLocation } from './scan-location';

@Injectable({
  providedIn: 'root'
})
export class ScanLocationService {

  constructor(
    private http: HttpClient
  ) { }

  public getAllScanLocations(): Observable<Response<any>> {
    return this.http
      .get<Response<any>>(`${environment.baseURL}scan`)
      .pipe(timeout(10000));
  }

  public deleteScanLocation(id: number): Observable<Response<any>> {
    return this.http
      .delete<Response<any>>(`${environment.baseURL}scan?id=${id}`)
      .pipe(timeout(10000));
  }

  public createNewScanLocation(location: ScanLocation): Observable<Response<any>> {
    const data = {path: location.path};
    return this.http
      .post<Response<any>>(`${environment.baseURL}scan/create`, data)
      .pipe(timeout(10000));
  }

  public editScanLocation(location: ScanLocation): Observable<Response<any>> {
    return this.http
      .put<Response<any>>(`${environment.baseURL}scan/edit`, location)
      .pipe(timeout(10000));
  }
}
