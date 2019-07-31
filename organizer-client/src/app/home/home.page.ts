import { Component, OnInit } from '@angular/core';
import { environment } from 'src/environments/environment';
import { MediaFile } from '../media/media.file';
import { FolderService } from '../folders/folder-service';
import { Folder } from '../folders/folder';
import { MediaServiceService } from '../media/media-service.service';
import { ToastController, ModalController, PopoverController } from '@ionic/angular';
import { AddTagComponent } from './add-tag/add-tag.component';
import { TagModel } from '../tag/tagModel';
import { TagService } from '../tag/tag.service';
import { ToastingService } from '../toasting.service';

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
})
export class HomePage implements OnInit {

  public truncationLimit = 20;
  knownTags: TagModel[];

  rootFolders: Folder[];
  currentFolder: Folder;
  previousFolders: Folder[] = new Array();

  constructor(
    private folderService: FolderService,
    private tagService: TagService,
    private mediaFileService: MediaServiceService,
    private modalController: ModalController,
    public toastController: ToastController,
    private toastingController: ToastingService,
    public popoverController: PopoverController
  ) {}

  ngOnInit(): void {
    this.folderService.getRootFolder().subscribe(res => {
      console.log(res);
      this.rootFolders = res.data.Folders;
    }, (err) => {
      console.log(`Could not get root folder`);
    });

    this.tagService.getAllTags().subscribe(res => {
      this.knownTags = res.data;
    }, (err) => {
      console.log(`Could not get tags`);
    });
  }

  folderClicked(folder: Folder) {
    console.log(folder);
    if (this.currentFolder) {
      this.previousFolders.push(this.currentFolder);
    }
    this.currentFolder = folder;
  }

  public backButton() {
    console.log("back clicked");
    this.currentFolder = this.previousFolders.pop();
  }

  async presentPopover(ev: any, mediaFile: MediaFile) {
    const popover = await this.popoverController.create({
      component: AddTagComponent,
      componentProps: {
        mediasTags: JSON.parse(JSON.stringify(mediaFile.tags)),  // Send in a cloned version so edits are not reflected
        knownTags: this.knownTags
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

  public hideMedia(file: MediaFile) {
    this.toastController.create({
      position: "bottom",
      color: "success",
      message: file.name + ' has been removed!',
      duration: 2000
    }).then((toast) => {
      toast.present();
    });
    //this.mediaFileService.setMediaFileAsHidden(hash).
  }
}
