import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { TagModel } from 'src/app/tags/tagModel';
import { MediaFile } from '../media.file';
import { MatDialog, MAT_TOOLTIP_DEFAULT_OPTIONS } from '@angular/material';
import { MediaTagsComponent } from '../media-tags/media-tags.component';
import { MediaInfoComponent } from '../media-info/media-info.component';
import { tooltipDefaultOptions } from 'src/app/common/constants';

@Component({
  selector: 'app-media-list-grid-item',
  templateUrl: './media-list-grid-item.component.html',
  styleUrls: ['./media-list-grid-item.component.scss'],
  providers: [
    { provide: MAT_TOOLTIP_DEFAULT_OPTIONS, useValue: tooltipDefaultOptions }
  ],
})
export class MediaListGridItemComponent implements OnInit {

  @Input() modifyMultiple: boolean;
  @Input() knownTags: TagModel[];
  @Input() file: MediaFile;
  @Input() imgSource: string;
  @Output() tagsEdited: EventEmitter<any> = new EventEmitter<any>();
  @Output() openMedia: EventEmitter<any> = new EventEmitter();
  @Output() tagClicked: EventEmitter<any> = new EventEmitter();
  @Output() favoriteToggled: EventEmitter<any> = new EventEmitter();
  @Output() checkboxClicked: EventEmitter<any> = new EventEmitter<any>();

  constructor(
    private dialog: MatDialog
  ) { }

  ngOnInit() {
  }

  showTags() {
    const dialogRef = this.dialog.open(MediaTagsComponent, {
      width: '500px',
      data: {
        mediaFiles: JSON.parse(JSON.stringify([this.file])),
        knownTags: JSON.parse(JSON.stringify(this.knownTags)),
        title: this.file.name
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        const data = { file: this.file, editedTags: result[0].tags };
        console.log(data);
        this.tagsEdited.emit(data);
      }
    });
  }

  showMediaInfo() {
    const dialogRef = this.dialog.open(MediaInfoComponent, {
      width: '720px',
      data: {
        file: this.file
      }
    });

    dialogRef.afterClosed().subscribe(result => {
    });
  }

  openMediaClick() {
    if (this.file.mimetype.includes('video')) {
      this.openMedia.emit(this.file);
    }
  }

  viewMediaWithTag(tag: TagModel) {
    this.tagClicked.emit(tag);
  }

  favoriteToggle() {
    this.favoriteToggled.emit(this.file);
  }

  checkboxClickedMethod(file: MediaFile) {
    file.isSelected = !file.isSelected;
    this.checkboxClicked.next();
  }

}
