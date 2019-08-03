import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { MediaFile } from './media.file';
import { Observable } from 'rxjs';
import { Response } from '../response';
import { HttpClient, HttpParams } from '@angular/common/http';
import { timeout } from 'rxjs/operators';
import { TagModel } from '../tag/tagModel';
import { PageRequest } from '../common/PageRequest';

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

  public updateMediaFileTags(id: number, updatedTags: TagModel[]): Observable<Response<any>> {
    return this.http
      .put<Response<any>>(`${environment.baseURL}media/tags`, {
        mediaId: id, tagIds: updatedTags.map(temp => temp.id)
      })
      .pipe(timeout(10000));
  }

  public getMediaPages(pageNumber: number) {
    const data = new PageRequest(pageNumber).toHttpParams();
    return this.http
      .get<Response<any>>(`${environment.baseURL}media`, { params: data})
      .pipe(timeout(10000));
  }
}
