import { Component, OnInit } from '@angular/core';
import { Folder } from './folder';
import { FolderService } from './folder.service';
import { TagService } from '../tags/tag.service';
import { TagModel } from '../tags/tagModel';
import { MediaService } from '../media/media.service';
import { AlertService } from '../alert/alert.service';
import { ResourceService } from '../media/resource.service';
import { MediaFile } from '../media/media.file';

@Component({
  selector: 'app-folder',
  templateUrl: './folder.component.html',
  styleUrls: ['./folder.component.scss']
})
export class FolderComponent implements OnInit {

  currentFolder: Folder;
  knownTags: TagModel[];
  rootFolders: Folder[];
  previousFolders: Folder[] = [];

  constructor(
    private alertService: AlertService,
    private folderService: FolderService,
    private mediaFileService: MediaService,
    private resourceService: ResourceService,
    private tagService: TagService
  ) { }

  ngOnInit() {
    this.folderService.getRootFolders().subscribe(res => {
      console.log(res);
      this.rootFolders = res.data.Folders;

      //TODO: DEBUG CODE REMOVE THIS. JUST FOR EASY QUICK REFRESHES
      //this.currentFolder = this.rootFolders[0];
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
    if (this.currentFolder) {
      this.previousFolders.push(this.currentFolder);
    }
    this.currentFolder = folder;
  }

  backClicked() {
    this.currentFolder = this.previousFolders.pop();
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

  updateMediaFilesTags(event: any) {
    this.mediaFileService.updateMediaFileTags(event.file.id, event.editedTags).subscribe(result => {
      event.file.tags = result.data.tags;
      this.alertService.success(`Successfully edited tags for file`);
    }, error => {
      this.alertService.error(`Failed to edit tags: ${error.error.error}`);
    });
  }

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

    // if URL couldn't be generated, fall back to this.
    if (!url) {
      url = 'https://i.kym-cdn.com/photos/images/newsfeed/001/460/439/32f.jpg';
    }
    return url;
  }

}
