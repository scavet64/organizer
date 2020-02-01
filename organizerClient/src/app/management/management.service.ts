import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { timeout } from 'rxjs/operators';
import { Response } from '../common/response';

@Injectable({
  providedIn: 'root'
})
export class ManagementService {

  constructor(
    private http: HttpClient
  ) { }

  public clearScannedMedia(): Observable<Response<any>> {
    return this.http
      .get<Response<any>>(`${environment.baseURL}management`)
      .pipe(timeout(10000));
  }
}
