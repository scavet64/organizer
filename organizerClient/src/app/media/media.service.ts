import { Injectable } from '@angular/core';
import { Response } from '../common/response';
import { HttpClient } from '@angular/common/http';
import { MediaFile } from './media.file';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';
import { timeout } from 'rxjs/operators';
import { TagModel } from '../tags/tagModel';
import { MediaSearchRequest } from './requests/media-search-request';
import { ResourceService } from './resource.service';
import { EditMultipleMediatagsRequest, MediaTags } from './requests/edit-multiple-mediatags-request';

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

  public getVideoStreamURLTranscode(video: MediaFile) {
    return `${environment.baseURL}media/${video.hash}/transcode`;
  }

  public getMediaDetails(media: MediaFile) {
    return this.http
      .get<Response<any>>(`${environment.baseURL}media/${media.hash}/info`)
      .pipe(timeout(10000));
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

  public updateMultipleMediaFileTags(updatedMedia: MediaFile[]): Observable<Response<any>> {

    const request: EditMultipleMediatagsRequest = new EditMultipleMediatagsRequest();

    updatedMedia.forEach(media => {
      request.updatedMedia.push(new MediaTags(media.id, media.tags.map(tag => tag.id)));
    });

    return this.http
      .put<Response<any>>(`${environment.baseURL}media/tags/multiple`, request)
      .pipe(timeout(10000));
  }

  public getMediaPagesSearch(searchRequest: MediaSearchRequest) {
    const data = searchRequest.toHttpParams();
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

  public toggleFavorite(id: number, favorite: boolean): Observable<Response<any>> {
    const data = {
      mediaId: id,
      isFavorite: favorite
    };
    return this.http
      .put<Response<any>>(`${environment.baseURL}media/favorite`, data)
      .pipe(timeout(10000));
  }

  public getRandomVideo(): Observable<Response<any>> {
    return this.http
      .get<Response<any>>(`${environment.baseURL}media/random/video`)
      .pipe(timeout(10000));
  }
}
