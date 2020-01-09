import { Component, OnInit } from '@angular/core';
import { Folder } from './folder';
import { FolderService } from './folder.service';
import { TagService } from '../tags/tag.service';
import { TagModel } from '../tags/tagModel';
import { MediaService } from '../media/media.service';
import { AlertService } from '../alert/alert.service';
import { ActivatedRoute, Router } from '@angular/router';
import { switchMap } from 'rxjs/operators';
import { VideoplayerService } from '../videoplayer/videoplayer.service';
import { MediaFile } from '../media/media.file';

export interface SearchParams {
  currentFolder: number;
  search: string;
}

@Component({
  selector: 'app-folder',
  templateUrl: './folder.component.html',
  styleUrls: ['./folder.component.scss']
})
export class FolderComponent implements OnInit {

  // This does not reload during navigation events
  knownTags: TagModel[];

  // These variables should be reloaded during navigation events
  rootFolders: Folder[];
  currentFolder: Folder;
  previousFolders: Folder[] = [];
  hadParams: boolean;

  constructor(
    private activatedRoute: ActivatedRoute,
    private alertService: AlertService,
    private folderService: FolderService,
    public mediaFileService: MediaService,
    private router: Router,
    private tagService: TagService,
    public videoplayerService: VideoplayerService
  ) { }

  ngOnInit() {
    this.activatedRoute.queryParams
      .pipe(
        switchMap(params => {
          console.log(params);
          if (params.folder) {
            this.hadParams = true;
            return this.folderService.getFolder(params.folder);
          } else {
            this.hadParams = false;
            return this.folderService.getRootFolders();
          }
        }))
      .subscribe(resp => {
        console.log(resp);
        if (this.hadParams) {
          this.currentFolder = resp.data;

          // Build out the breadcrumbs again
          this.previousFolders = [];
          let previous = this.currentFolder.folder;
          while (previous != null) {
            this.previousFolders.push(previous);
            previous = previous.folder;
          }
          this.previousFolders.reverse();
        } else {
          this.currentFolder = null;
          this.rootFolders = resp.data.Folders;
        }
      });

    this.tagService.getAllTags().subscribe(res => {
      this.knownTags = res.data;
      //console.log(this.knownTags);
    }, (err) => {
      console.log(`Could not get tags`);
    });
  }

  folderClicked(folder: Folder) {
    this.router.navigate(['/folder'], {
      queryParams: { folder: folder.id },
      queryParamsHandling: 'merge'
    });
  }

  backClicked() {
    const navigateTo = this.previousFolders.pop();
    if (navigateTo) {
      this.folderClicked(navigateTo);
    } else {
      this.router.navigate(['/folder'], {
        queryParams: {},
      });
    }
  }

  updateMediaFilesTags(event: any) {
    this.mediaFileService.updateMediaFileTags(event.file.id, event.editedTags).subscribe(result => {
      event.file.tags = result.data.tags;
      this.alertService.success(`Successfully edited tags for file`);
    }, error => {
      this.alertService.error(`Failed to edit tags: ${error.error.error}`);
    });
  }

  openMedia(mediaFile: MediaFile) {
    this.videoplayerService.showVideo(mediaFile);
  }

}
