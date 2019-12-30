import { Injectable } from '@angular/core';
import { Response } from '../common/response';
import { HttpClient } from '@angular/common/http';
import { MediaFile } from './media.file';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';
import { timeout } from 'rxjs/operators';
import { TagModel } from '../tags/tagModel';
import { PageRequest } from '../common/PageRequest';
import { MediaSearchRequest } from './media-search-request';


@Injectable({
  providedIn: 'root'
})
export class MediaService {

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

  public getMediaPages(pageNumber: number, numberPerPage: number = 20) {
    const data = new PageRequest(pageNumber, numberPerPage).toHttpParams();
    return this.http
      .get<Response<any>>(`${environment.baseURL}media`, { params: data })
      .pipe(timeout(10000));
  }

  public getMediaPagesSearch(pageNumber: number, name: string, tags: TagModel[]) {
    const data = new MediaSearchRequest(pageNumber, name, tags).toHttpParams();
    return this.http
      .get<Response<any>>(`${environment.baseURL}media`, { params: data })
      .pipe(timeout(10000));
  }
}
