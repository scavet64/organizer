import { Component, OnInit } from '@angular/core';
import { MediaServiceService } from './media-service.service';
import { PaginationResponse } from '../common/page-response';
import { MediaFile } from './media.file';
import { PopoverController } from '@ionic/angular';
import { MediaTagsComponent } from '../media-tags/media-tags.component';
import { TagService } from '../tag/tag.service';
import { TagModel } from '../tag/tagModel';

@Component({
  selector: 'app-media',
  templateUrl: './media.page.html',
  styleUrls: ['./media.page.scss'],
})
export class MediaPage implements OnInit {

  sizeXl = 4;
  sizeLg = 6;
  sizeMd = 8;
  sizeSm = 10;
  sizeXs = 12;

  readonly pageSizeOptions = [10, 20, 50];
  pageResponse: PaginationResponse<MediaFile>;
  knownTags: TagModel[];
  isListView = true;
  searchBox: string;

  constructor(
    private mediaFileService: MediaServiceService,
    private tagService: TagService
  ) { }

  ngOnInit() {
    this.getMedia();

    this.tagService.getAllTags().subscribe(res => {
      this.knownTags = res.data;
      console.log(this.knownTags);
    }, (err) => {
      console.log(`Could not get tags`);
    });
  }

  public getMedia() {
    this.mediaFileService.getMediaPages(0).subscribe(res => {
      this.pageResponse = res.data;
      console.log(this.pageResponse);
    });
  }

  onPageChange(event) {
    console.log(event);
    console.log(event.pageIndex);
    this.mediaFileService.getMediaPages(event.pageIndex).subscribe(res => {
      this.pageResponse = res.data;
    });
  }

  search() {
    this.mediaFileService.getMediaPagesSearch(0, this.searchBox, null).subscribe(res => {
      this.pageResponse = res.data;
      console.log(this.pageResponse);
    });
  }

  // async presentPopover(ev: any, mediaFile: MediaFile) {
  //   const popover = await this.popoverController.create({
  //     component: MediaTagsComponent,
  //     componentProps: {
  //       mediasTags: JSON.parse(JSON.stringify(mediaFile.tags)),  // Send in a cloned version so edits are not reflected
  //       knownTags: this.knownTags
  //     },
  //     cssClass: 'tags-popover',
  //     event: ev,
  //     translucent: true
  //   });
  //   await popover.present();

  //   popover.onDidDismiss()
  //     .then((data) => {
  //       const updatedTags = data.data;
  //       if (updatedTags) {
  //         this.mediaFileService.updateMediaFileTags(mediaFile.id, updatedTags).subscribe(res => {

  //           mediaFile.tags = res.data.tags;

  //           this.toastingController.showSuccessToast(`Successfully updated tags`);
  //         }, (err) => {
  //           console.log(`Could not update tags: ${err.error.error}`);
  //           this.toastingController.showPersistentErrorToast(`Could not update tags: ${err.error.error}`);
  //         });
  //       }
  //     });
  // }

  // async showFullscreen(toShow: MediaFile) {
  //   const popover = await this.modalController.create({
  //     component: VideoplayerComponent,
  //     componentProps: {
  //       mediaFile: toShow
  //     },
  //     cssClass: 'videoplayer'
  //   });
  //   await popover.present();
  // }

  public getVideoUrl(file: MediaFile): string {
    return this.mediaFileService.getVideoStreamURL(file);
  }

  // public hideMedia(file: MediaFile) {
  //   this.toastController.create({
  //     position: "bottom",
  //     color: "success",
  //     message: file.name + ' has been removed!',
  //     duration: 2000
  //   }).then((toast) => {
  //     toast.present();
  //   });
  //   //this.mediaFileService.setMediaFileAsHidden(hash).
  // }

  // public navigateToFolder(folder: Folder) {
  //   let simp: Folder;
  //   let found = false;
  //   console.log("previous folders Array:");
  //   console.log(this.previousFolders);
  //   while (!found && this.previousFolders.length > 0) {
  //     simp = this.previousFolders.pop();
  //     console.log(simp);
  //     if (simp.id === folder.id) {
  //       console.log("found");
  //       found = true;
  //     }
  //   }

  //   console.log("previous folders Array After:");
  //   console.log(this.previousFolders);

  //   this.currentFolder = simp;
  // }

  // public changeZoom(newSize: number) {
  //   if (newSize >= this.minZoom && newSize <= this.maxZoom) {
  //     this.mediaColumnSize = newSize;
  //   }
  // }

  // public decreaseZoom() {
  //   this.sizeArray.forEach(element => {
  //     if (element > 0) {
  //       element = element - this.stepZoom;
  //     }
  //   });

  //   this.sizeXl = this.sizeXl - this.stepZoom;
  //   this.sizeLg = this.sizeLg - this.stepZoom;
  //   this.sizeMd = this.sizeMd - this.stepZoom;
  //   this.sizeSm = this.sizeSm - this.stepZoom;
  //   this.sizeXs = this.sizeXs - this.stepZoom;
  // }

  // public increaseZoom() {

  //   this.sizeArray.forEach(element => {
  //     if (element < 12) {
  //       element = element + this.stepZoom;
  //     }
  //   });

  //   this.sizeXl = this.sizeXl + this.stepZoom;
  //   this.sizeLg = this.sizeLg + this.stepZoom;
  //   this.sizeMd = this.sizeMd + this.stepZoom;
  //   this.sizeSm = this.sizeSm + this.stepZoom;
  //   this.sizeXs = this.sizeXs + this.stepZoom;
  // }

  // public setListMode(isList: boolean) {
  //   this.isListView = isList;
  // }

}
