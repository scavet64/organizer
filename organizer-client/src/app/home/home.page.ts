import { Component, OnInit } from '@angular/core';
import { environment } from 'src/environments/environment';
import { MediaFile } from '../media/media.file';
import { FolderService } from '../folders/folder-service';
import { Folder } from '../folders/folder';
import { MediaServiceService } from '../media/media-service.service';
import { ToastController } from '@ionic/angular';

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
})
export class HomePage implements OnInit {
  rootFolders: Folder[];
  tempFolders: Folder[];
  rootFiles: MediaFile[];



  constructor(
    private folderService: FolderService,
    private mediaFileService: MediaServiceService,
    public toastController: ToastController,
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
