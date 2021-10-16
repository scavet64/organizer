import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { MediaFile } from '../media.file';
import { MatDialog, MAT_TOOLTIP_DEFAULT_OPTIONS } from '@angular/material';
import { MediaTagsComponent } from '../media-tags/media-tags.component';
import { TagModel } from 'src/app/tags/tagModel';
import { tooltipDefaultOptions } from 'src/app/common/constants';
import { MediaInfoComponent } from '../media-info/media-info.component';

@Component({
  selector: 'app-media-list-item',
  templateUrl: './media-list-item.component.html',
  styleUrls: ['./media-list-item.component.scss'],
  providers: [
    { provide: MAT_TOOLTIP_DEFAULT_OPTIONS, useValue: tooltipDefaultOptions }
  ],
})
export class MediaListItemComponent implements OnInit {

  @Input() isOdd: boolean;
  @Input() modifyMultiple: boolean;
  @Input() knownTags: TagModel[];
  @Input() file: MediaFile;
  @Input() imgSource: string;
  @Output() tagsEdited: EventEmitter<any> = new EventEmitter<any>();
  @Output() openMedia: EventEmitter<any> = new EventEmitter();
  @Output() tagClicked: EventEmitter<any> = new EventEmitter();
  @Output() favoriteToggled: EventEmitter<any> = new EventEmitter();
  @Output() checkboxClicked: EventEmitter<any> = new EventEmitter<any>();
  @Output() openInDeoVRClicked: EventEmitter<any> = new EventEmitter<any>();
  @Output() ignoreMediaClicked: EventEmitter<any> = new EventEmitter<any>();

  constructor(
    private dialog: MatDialog
  ) { }

  ngOnInit() {
  }

  showTags() {
    const dialogRef = this.dialog.open(MediaTagsComponent, {
      width: '500px',
      data: {
        mediasTags: JSON.parse(JSON.stringify(this.file.tags)),  // Send in a cloned version so edits are not reflected
        knownTags: this.knownTags,
        filename: this.file.name
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        const data = { file: this.file, editedTags: result };
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
    this.openMedia.emit(this.file);
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

  openInDeoVR() {
    this.openInDeoVRClicked.emit(this.file);
  }

  ignoreMedia() {
    this.ignoreMediaClicked.emit(this.file);
  }
}
