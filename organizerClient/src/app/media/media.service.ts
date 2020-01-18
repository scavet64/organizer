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
import { ResourceService } from './resource.service';

@Injectable({
  providedIn: 'root'
})
export class MediaService {

  constructor(
    private http: HttpClient,
    private resourceService: ResourceService
  ) { }

  public getVideoStreamURL(video: MediaFile) {
    return `${environment.baseURL}media/${video.hash}/full`;
  }

  public setMediaFileAsHidden(hash: string): Observable<Response<any>> {
    return this.http
      .get<Response<any>>(`${environment.baseURL}media/hide/${hash}`)
      .pipe(timeout(10000));
  }

  public updateMediaFileTags(id: number, updatedTags: TagModel[]): Observable<Response<any>> {
    return this.http
      .put<Response<any>>(`${environment.baseURL}media/tags`, {
        mediaId: id, tagIds: updatedTags.map(temp => temp.id)
      })
      .pipe(timeout(10000));
  }

  public getMediaPages(pageNumber: number, numberPerPage: number = 20, sortColumn: string, sortDirection: string) {
    const data = new PageRequest(pageNumber, numberPerPage, sortColumn, sortDirection).toHttpParams();
    return this.http
      .get<Response<any>>(`${environment.baseURL}media`, { params: data })
      .pipe(timeout(10000));
  }

  public getMediaPagesSearch(pageNumber: number, name: string, tags: TagModel[], sortColumn: string, sortDirection: string) {
    const data = new MediaSearchRequest(pageNumber, name, tags, sortColumn, sortDirection).toHttpParams();
    return this.http
      .get<Response<any>>(`${environment.baseURL}media`, { params: data })
      .pipe(timeout(10000));
  }

  public getThumbnailSrc(mediaFile: MediaFile): string {
    let url;
    if (mediaFile.mimetype.includes('video')) {
      // This is a video so use a thumbnail if it has one.
      if (mediaFile.thumbnail) {
        url = this.resourceService.getThumbnailUrl(mediaFile.thumbnail);
      }
    } else if (mediaFile.mimetype.includes('image')) {
      url = this.resourceService.getMediaUrl(mediaFile);
    }

    // if URL couldn't be generated, fall back to this.
    if (!url) {
      url = 'https://i.kym-cdn.com/photos/images/newsfeed/001/460/439/32f.jpg';
    }
    return url;
  }

  public addView(id: number): Observable<Response<any>> {
    return this.http
      .put<Response<any>>(`${environment.baseURL}media/view`, id)
      .pipe(timeout(10000));
  }
}
