import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { MediaFile } from './media.file';
import { Observable } from 'rxjs';
import { Response } from '../response';
import { HttpClient } from '@angular/common/http';
import { timeout } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class MediaServiceService {

  constructor(
    private http: HttpClient
  ) { }

  public getVideoStreamURL(video: MediaFile) {
    return `${environment.baseURL}media/${video.hash}/full`;
  }

  public setMediaFileAsHidden(hash: string): Observable<Response<any>> {
    return this.http
    .get<Response<any>>(`${environment.baseURL}media/hide/${hash}`)
    .pipe(timeout(10000))
  }
}
