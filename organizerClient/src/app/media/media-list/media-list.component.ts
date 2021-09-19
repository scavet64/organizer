import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MatDialog } from '@angular/material';
import { ActivatedRoute, Router } from '@angular/router';
import { AlertService } from 'src/app/alert/alert.service';
import { PaginationResponse } from 'src/app/common/page-response';
import { TagService } from 'src/app/tags/tag.service';
import { TagModel } from 'src/app/tags/tagModel';
import { VideoplayerService } from 'src/app/videoplayer/videoplayer.service';
import { MediaTagsComponent } from '../media-tags/media-tags.component';
import { MediaFile } from '../media.file';
import { MediaService } from '../media.service';
import { ResourceService } from '../resource.service';

@Component({
  selector: 'app-media-list',
  templateUrl: './media-list.component.html',
  styleUrls: ['./media-list.component.scss']
})
export class MediaListComponent implements OnInit {

  readonly pageSizeOptions = [10, 20, 50];

  @Input() pageResponse: PaginationResponse<MediaFile>;
  @Input() selectedMediaIndex: any;
  @Input() isListView: boolean;
  @Output() pageChange: EventEmitter<any> = new EventEmitter<any>();
  @Output() tagClicked: EventEmitter<any> = new EventEmitter<any>();
  @Output() selectedMediaChange: EventEmitter<any> = new EventEmitter<any>();

  editingMultiple = false;
  areAllChecked = false;
  isIndeterminate = false;

  knownTags: TagModel[];                // All known tags in the application

  constructor(
    private alertService: AlertService,
    private dialog: MatDialog,
    private mediaFileService: MediaService,
    private resourceService: ResourceService,
    private tagService: TagService,
    public videoplayerService: VideoplayerService,
    public router: Router
  ) {
  }

  ngOnInit() {
    this.tagService.getAllTags().subscribe(res => {
      this.knownTags = res.data;
    });
    console.log(`selectedIndex is: ${this.selectedMediaIndex}`);
  }

  ///////////////// Checkbox and multiple tag editing /////////////////

  editMultiple() {
    this.editingMultiple = true;
  }

  cancelEditMultiple() {
    this.editingMultiple = false;
    this.pageResponse.content.forEach(file => file.isSelected = false);
  }

  /**
   * Callback method for when a checkbox inside of the list is clicked.
   * This will maintain the current state of the indeterminate or all checked
   */
  checkboxClicked() {
    const selected = this.pageResponse.content.filter(file => file.isSelected);
    if (selected.length === 0) {
      this.areAllChecked = false;
      this.isIndeterminate = false;
    } else if (selected.length === this.pageResponse.content.length) {
      this.isIndeterminate = false;
      this.areAllChecked = true;
    } else {
      this.isIndeterminate = true;
      this.areAllChecked = false;
    }
  }

  /**
   * Callback for when the check-all checkbox is clicked. If the current
   * state is indeterminate, the rest of the boxes will be checked.
   */
  allCheckClicked() {
    if (this.isIndeterminate) {
      this.areAllChecked = true;
      this.isIndeterminate = false;
    }
    this.pageResponse.content.forEach(file => file.isSelected = this.areAllChecked);
  }

  chooseSelectedMediasTags() {
    const checkedMedia = this.pageResponse.content.filter(file => file.isSelected);

    if (checkedMedia.length === 0) {
      this.alertService.warning(`No media selected`);
      return;
    }

    const dialogRef = this.dialog.open(MediaTagsComponent, {
      width: '500px',
      data: {
        mediaFiles: JSON.parse(JSON.stringify(checkedMedia)),   // Send in a cloned version so edits are not reflected
        knownTags: JSON.parse(JSON.stringify(this.knownTags)),  // Send in a cloned version so edits are not reflected
        title: `Select tags for media`
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.mediaFileService.updateMultipleMediaFileTags(result).subscribe(res => {
          const updated: MediaFile[] = res.data;
          updated.forEach(updatedMedia => {
            checkedMedia.find(obj => obj.id === updatedMedia.id).tags = updatedMedia.tags;
          });
          this.alertService.success(`Successfully tagged media`);
          this.cancelEditMultiple();
        });
      }
    });

    console.log(`total checked: ${checkedMedia.length}`);
  }

  updateMediaFilesTags(event: any) {
    this.mediaFileService.updateMediaFileTags(event.file.id, event.editedTags).subscribe(result => {
      event.file.tags = result.data.tags;
      this.alertService.success(`Successfully edited tags for file`);
    }, error => {
      this.alertService.error(`Failed to edit tags: ${error.error.error}`);
    });
  }

  ////////////////////////////////////////////////////////////////////

  getThumbnailSrc(mediaFile: MediaFile): string {
    let url;
    if (mediaFile.mimetype.includes('video')) {
      // This is a video so use a thumbnail if it has one.
      if (mediaFile.thumbnail) {
        url = this.resourceService.getThumbnailUrl(mediaFile.thumbnail);
      }
    } else if (mediaFile.mimetype.includes('image')) {
      url = this.resourceService.getMediaUrl(mediaFile);
    }
    // TODO: Add Audio Thumbnail Icon

    // if URL couldn't be generated, fall back to this.
    if (!url) {
      url = 'https://i.kym-cdn.com/photos/images/newsfeed/001/460/439/32f.jpg';
    }
    return url;
  }

  openMedia(file: MediaFile) {
    if (file.mimetype.includes('video')) {
      this.videoplayerService.showVideo(file);
    } else {
      window.open(this.resourceService.getMediaUrl(file), '_blank');
      //this.router.navigateByUrl(this.resourceService.getMediaUrl(file));
    }
  }

  favoriteToggle(mediaFile: MediaFile) {
    this.mediaFileService.toggleFavorite(mediaFile.id, !mediaFile.isFavorite).subscribe(res => {
      mediaFile.isFavorite = res.data.isFavorite;
      this.alertService.success(`Successfully set media as a favorite`);
    });
  }

  openInDeoVR(mediaFile: MediaFile) {
    this.mediaFileService.openFileInDeoVR(mediaFile.hash).subscribe(res => {
      this.alertService.success(`Successfully opened in DeoVR`);
    })
  }

}
