import { Component, OnInit } from '@angular/core';
import { environment } from 'src/environments/environment';
import { MediaFile } from '../media/media.file';
import { FolderService } from '../folders/folder-service';
import { Folder } from '../folders/folder';
import { MediaServiceService } from '../media/media-service.service';
import { ToastController, ModalController, PopoverController } from '@ionic/angular';
import { AddTagComponent } from './add-tag/add-tag.component';

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
})
export class HomePage implements OnInit {

  public truncationLimit = 20;

  rootFolders: Folder[];
  tempFolders: Folder[];
  rootFiles: MediaFile[];

  constructor(
    private folderService: FolderService,
    private mediaFileService: MediaServiceService,
    private modalController: ModalController,
    public toastController: ToastController,
    public popoverController: PopoverController
  ) {}

  ngOnInit(): void {
    this.folderService.getRootFolder().subscribe(res => {
      console.log(res);
      this.rootFolders = res.data.Folders;
      this.tempFolders = this.rootFolders[0].folders;
      this.rootFiles = this.rootFolders[0].files;
      console.log(this.tempFolders);
      // const file = res;
      // const reader = new FileReader();
      // reader.readAsDataURL(file);
      // reader.onloadend = (e) => {
      //   const image = reader.result;
      //   this.videoDoc.setThumbnailUrl(this.sanitizer.bypassSecurityTrustUrl(image.toString()));
      // };
    });
  }

  async presentPopover(ev: any, mediaFile: MediaFile) {
    const popover = await this.popoverController.create({
      component: AddTagComponent,
      componentProps: {
        taggedMedia: mediaFile
      },
      cssClass: 'tags-popover',
      event: ev,
      translucent: true
    });
    return await popover.present();
  }

  public getVideoUrl(file: MediaFile): string {
    return this.mediaFileService.getVideoStreamURL(file);
  }

  public addTag(file: MediaFile) {

    // const modal = await this.modalController.create({
    //   component: AddTagComponent,
    //   cssClass: 'my-custom-modal-css'
    // });
    // await modal.present();

    // modal.onDidDismiss()
    //   .then((data) => {
    //     const tag = data.data;
    //     // If the tag was edited.
    //     if (tag) {
    //       this.tagService.editTag(tag).subscribe(res => {
    //         const editedTag = res.data;
    //         this.toastingService.showSuccessToast(`Successfully edited Tag!`);

    //         // Edit the tag already inside the table using the returned data
    //         tagToEdit.backgroundColor = editedTag.backgroundColor;
    //         tagToEdit.name = editedTag.nam;
    //         tagToEdit.id = editedTag.id;
    //         tagToEdit.name = editedTag.name;
    //         tagToEdit.textColor = editedTag.textColor;
    //       }, (err) => {
    //         this.toastingService.showPersistentErrorToast(`Could not edit tag successfully: ${err.error.error}`);
    //       });
    //     }
    //   });
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
