import { Injectable } from '@angular/core';
import { StoredFile } from './stored.file';
import { environment } from 'src/environments/environment';
import { MediaFile } from './media.file';

@Injectable({
  providedIn: 'root'
})
export class ResourceService {

  constructor() { }

  public getThumbnailUrl(file: StoredFile) {
    return `${environment.baseURL}stored/${file.hash}/full`;
  }

  public getMediaUrl(file: MediaFile) {
    return `${environment.baseURL}media/${file.hash}/full`;
  }
}
