import { TagModel } from 'src/app/tags/tagModel';

export class EditMultipleMediatagsRequest {
  updatedMedia: MediaTags[] = [];
}

export class MediaTags {
  mediaId: number;
  tagIds: number[];

  constructor(mediaId: number, tagIds: number[]) {
    this.mediaId = mediaId;
    this.tagIds = tagIds;
  }
}
