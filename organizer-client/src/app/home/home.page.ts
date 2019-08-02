import { Component, OnInit } from '@angular/core';
import { environment } from 'src/environments/environment';
import { MediaFile } from '../media/media.file';
import { FolderService } from '../folders/folder-service';
import { Folder } from '../folders/folder';
import { MediaServiceService } from '../media/media-service.service';
import { ToastController, ModalController, PopoverController, Platform } from '@ionic/angular';
import { MediaTagsComponent } from '../media-tags/media-tags.component';
import { TagModel } from '../tag/tagModel';
import { TagService } from '../tag/tag.service';
import { ToastingService } from '../toasting.service';

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
})
export class HomePage implements OnInit {

  public stepZoom = 1;
  public minZoom = 2;
  public maxZoom = 12;
  public truncationLimit = 20;
  public mediaColumnSize = 4;

  public isListView = true;
  public searchBox: string;

  knownTags: TagModel[];
  rootFolders: Folder[];
  currentFolder: Folder;
  previousFolders: Folder[] = new Array();

  constructor(
    private folderService: FolderService,
    private tagService: TagService,
    private mediaFileService: MediaServiceService,
    private toastController: ToastController,
    private toastingController: ToastingService,
    private popoverController: PopoverController
  ) {}

  ngOnInit(): void {
    this.folderService.getRootFolder().subscribe(res => {
      console.log(res);
      this.rootFolders = res.data.Folders;

      //TODO: DEBUG CODE REMOVE THIS. JUST FOR EASY QUICK REFRESHES
      this.currentFolder = this.rootFolders[0];
    }, (err) => {
      console.log(`Could not get root folder`);
    });

    this.tagService.getAllTags().subscribe(res => {
      this.knownTags = res.data;
      console.log(this.knownTags);
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
      component: MediaTagsComponent,
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

  public navigateToFolder(folder: Folder) {
    let simp: Folder;
    let found = false;
    console.log("previous folders Array:");
    console.log(this.previousFolders);
    while (!found && this.previousFolders.length > 0) {
      simp = this.previousFolders.pop();
      console.log(simp);
      if (simp.id === folder.id) {
        console.log("found");
        found = true;
      }
    }

    console.log("previous folders Array After:");
    console.log(this.previousFolders);

    this.currentFolder = simp;
  }

  public changeZoom(newSize: number) {
    if (newSize >= this.minZoom && newSize <= this.maxZoom) {
      this.mediaColumnSize = newSize;
    }
  }
}
