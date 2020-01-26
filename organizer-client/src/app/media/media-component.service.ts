import { Injectable } from '@angular/core';
import { MediaServiceService } from './media-service.service';
import { PopoverController, ModalController } from '@ionic/angular';
import { ToastingService } from '../toasting.service';
import { MediaFile } from './media.file';
import { MediaTagsComponent } from '../media-tags/media-tags.component';
import { TagModel } from '../tag/tagModel';
import { VideoplayerComponent } from './videoplayer/videoplayer.component';

@Injectable({
  providedIn: 'root'
})
export class MediaComponentService {

  constructor(
    private mediaFileService: MediaServiceService,
    private popoverController: PopoverController,
    private toastingController: ToastingService,
    private modalController: ModalController
  ) { }

  async presentPopover(ev: any, mediaFile: MediaFile, tags: TagModel[]) {
    const popover = await this.popoverController.create({
      component: MediaTagsComponent,
      componentProps: {
        mediasTags: JSON.parse(JSON.stringify(mediaFile.tags)),  // Send in a cloned version so edits are not reflected
        knownTags: tags
      },
      cssClass: 'tags-popover',
      event: ev,
      translucent: true
    });
    await popover.present();

    popover.onDidDismiss()
      .then((data) => {
        const updatedTags = data.data;
        if (updatedTags) {
          this.mediaFileService.updateMediaFileTags(mediaFile.id, updatedTags).subscribe(res => {

            mediaFile.tags = res.data.tags;

            this.toastingController.showSuccessToast(`Successfully updated tags`);
          }, (err) => {
            console.log(`Could not update tags: ${err.error.error}`);
            this.toastingController.showPersistentErrorToast(`Could not update tags: ${err.error.error}`);
          });
        }
      });
  }

  public getVideoUrl(file: MediaFile): string {
    return this.mediaFileService.getVideoStreamURL(file);
  }

  async showFullscreen(toShow: MediaFile) {
    const popover = await this.modalController.create({
      component: VideoplayerComponent,
      componentProps: {
        mediaFile: toShow
      },
      cssClass: 'videoplayer'
    });
    await popover.present();
  }
}
