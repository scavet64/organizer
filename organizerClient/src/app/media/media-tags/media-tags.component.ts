import { Component, OnInit, Input, Inject } from '@angular/core';
import { TagModel } from 'src/app/tags/tagModel';
import { MAT_DIALOG_DATA, MatDialogRef, MAT_TOOLTIP_DEFAULT_OPTIONS } from '@angular/material';
import { tooltipDefaultOptions } from 'src/app/common/constants';
import { MediaFile } from '../media.file';

@Component({
  selector: 'app-media-tags',
  templateUrl: './media-tags.component.html',
  styleUrls: ['./media-tags.component.scss'],
  providers: [
    { provide: MAT_TOOLTIP_DEFAULT_OPTIONS, useValue: tooltipDefaultOptions }
  ],
})
export class MediaTagsComponent implements OnInit {

  searchBox: string;

  public knownTags: TagModel[];
  public mediaFiles: MediaFile[];
  public title: string;

  constructor(
    public dialogRef: MatDialogRef<MediaTagsComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) { }

  ngOnInit() {
    this.knownTags = this.data.knownTags;
    this.title = this.data.title;
    this.mediaFiles = this.data.mediaFiles;

    console.log(this.knownTags);
    this.knownTags.forEach(tag => {

      let atLeastOneHas = false;
      let allHaveThisTag = true;
      this.mediaFiles.forEach(mediaFile => {
        if (mediaFile.tags.find(x => x.id === tag.id)) {
          atLeastOneHas = true;
        } else {
          allHaveThisTag = false;
        }
      });
      if (atLeastOneHas) {
        if (!allHaveThisTag) {
          tag.indeterminate = true;
        } else {
          tag.selected = true;
        }
      }
    });
  }

  tagClicked(tag: TagModel) {
    console.log('testing click');

    // If the tag is indeterminate, they are now all checked.
    // If the media file didnt previously have the tag, add it
    if (tag.indeterminate) {
      this.mediaFiles.forEach(mediaFile => {
        const t = mediaFile.tags.find(x => x.id = tag.id);
        if (!t) {
          mediaFile.tags.push(tag);
        }
      });
      console.log('indeterminate tag modified');
    } else if (tag.selected) {
      // remove it from all the media
      this.mediaFiles.forEach(mediaFile => {
        mediaFile.tags = mediaFile.tags.filter(obj => obj.id !== tag.id);
      });
      console.log('tag removed from all');
    } else {
      // add it to all media.
      this.mediaFiles.forEach(mediaFile => {
        mediaFile.tags.push(tag);
      });
      console.log('tag added to all');
    }
  }

  onNoClick(): void {
    this.dialogRef.close(null);
  }

  save(): void {
    this.dialogRef.close(this.mediaFiles);
  }

}
