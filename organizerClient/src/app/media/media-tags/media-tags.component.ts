import { Component, OnInit, Input, Inject } from '@angular/core';
import { TagModel } from 'src/app/tags/tagModel';
import { MAT_DIALOG_DATA, MatDialogRef, MAT_TOOLTIP_DEFAULT_OPTIONS } from '@angular/material';
import { tooltipDefaultOptions } from 'src/app/common/constants';

@Component({
  selector: 'app-media-tags',
  templateUrl: './media-tags.component.html',
  styleUrls: ['./media-tags.component.scss'],
  providers: [
    {provide: MAT_TOOLTIP_DEFAULT_OPTIONS, useValue: tooltipDefaultOptions}
  ],
})
export class MediaTagsComponent implements OnInit {

  searchBox: string;

  public knownTags: TagModel[];
  public mediasTags: TagModel[];
  public filename: string;

  constructor(
    public dialogRef: MatDialogRef<MediaTagsComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) { }

  ngOnInit() {
    this.mediasTags = this.data.mediasTags;
    this.knownTags = this.data.knownTags;
    this.filename = this.data.filename;
    console.log(this.mediasTags);
    console.log(this.knownTags);
    this.knownTags.forEach(tag => {
      if (this.mediasTags.find(x => x.id === tag.id)) {
        tag.selected = true;
      } else {
        tag.selected = false;
      }
    });
  }

  tagClicked(tag: TagModel) {
    console.log('testing click');
    if (this.mediasTags.find(x => x.id === tag.id)) {
      this.mediasTags = this.mediasTags.filter(obj => obj.id !== tag.id);
      console.log('tag removed');
    } else {
      this.mediasTags.push(tag);
      console.log('tag added');
    }
  }

  onNoClick(): void {
    this.dialogRef.close(null);
  }

  save(): void {
    this.dialogRef.close(this.mediasTags);
  }

}
