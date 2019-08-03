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
import { VideoplayerComponent } from '../media/videoplayer/videoplayer.component';
import { CardItemComponent } from '../media/card-item/card-item.component';

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
})
export class HomePage implements OnInit {

  readonly IMAGE_PNG = 'image/png';
  public stepZoom = 1;
  public minZoom = 2;
  public maxZoom = 12;
  public truncationLimit = 20;
  public mediaColumnSize = 4;

  sliderLevel = 2;

  sizeXl = 4;
  sizeLg = 6;
  sizeMd = 8;
  sizeSm = 10;
  sizeXs = 12;

  private sizeArray = [this.sizeXl, this.sizeLg, this.sizeMd, this.sizeSm, this.sizeXs];

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
    private modalController: ModalController,
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

  public decreaseZoom() {
    this.sizeArray.forEach(element => {
      if (element > 0) {
        element = element - this.stepZoom;
      }
    });

    this.sizeXl = this.sizeXl - this.stepZoom;
    this.sizeLg = this.sizeLg - this.stepZoom;
    this.sizeMd = this.sizeMd - this.stepZoom;
    this.sizeSm = this.sizeSm - this.stepZoom;
    this.sizeXs = this.sizeXs - this.stepZoom;
  }

  public increaseZoom() {

    this.sizeArray.forEach(element => {
      if (element < 12) {
        element = element + this.stepZoom;
      }
    });

    this.sizeXl = this.sizeXl + this.stepZoom;
    this.sizeLg = this.sizeLg + this.stepZoom;
    this.sizeMd = this.sizeMd + this.stepZoom;
    this.sizeSm = this.sizeSm + this.stepZoom;
    this.sizeXs = this.sizeXs + this.stepZoom;
  }

  public setListMode(isList: boolean) {
    this.isListView = isList;
  }
}
