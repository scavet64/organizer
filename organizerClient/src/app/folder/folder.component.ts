import { Component, OnInit } from '@angular/core';
import { Folder } from './folder';
import { FolderService } from './folder.service';
import { TagService } from '../tags/tag.service';
import { TagModel } from '../tags/tagModel';
import { MediaService } from '../media/media.service';
import { AlertService } from '../alert/alert.service';
import { ActivatedRoute, Router } from '@angular/router';
import { flatMap, switchMap } from 'rxjs/operators';
import { VideoplayerService } from '../videoplayer/videoplayer.service';
import { MediaFile } from '../media/media.file';
import { PageRequest } from '../common/PageRequest';
import { PaginationResponse } from '../common/page-response';
import { Observable, Subscription } from 'rxjs';
import { FolderMediaSearchRequest } from './request/FolderMediaSearchRequest';

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

  pageResponse: PaginationResponse<MediaFile>;

  constructor(
    private activatedRoute: ActivatedRoute,
    private alertService: AlertService,
    private folderService: FolderService,
    public mediaFileService: MediaService,
    private router: Router,
    private tagService: TagService,
    public videoplayerService: VideoplayerService
  ) { }

  private params;

  ngOnInit() {
    this.activatedRoute.queryParams
      .pipe(
        flatMap(params => {
          console.log(params);
          this.params = params;
          if (params.folder) {
            this.hadParams = true;
            return this.folderService.getFolder(params.folder);
          } else {
            this.hadParams = false;
            return this.folderService.getRootFolders();
          }
        }),
        flatMap((resp) => {
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

            const pageIndex = this.params.pageIndex ? this.params.pageIndex : 0;
            const pageSize = this.params.pageSize ? this.params.pageSize : 20;
            return this.folderService.getFolderPage(this.currentFolder.id, new PageRequest(pageIndex, pageSize));
          } else {
            this.currentFolder = null;
            this.rootFolders = resp.data.Folders;
            return new Observable<any>();
          }
        }),
        flatMap((resp) => {
          if (resp && resp.data) {
            this.pageResponse = resp.data;
            console.log(this.pageResponse);
          }
          return this.tagService.getAllTags();
        }))
      .subscribe(resp => {
        this.knownTags = resp.data;
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

  onPageChange(event) {
    // this.search(event.pageIndex, event.pageSize);

    console.log(event);
    var params = {
      folder: this.currentFolder.id,
      ...(event.pageIndex !== 0 && { pageIndex: event.pageIndex }),
      ...(event.pageSize !== 20 && {pageSize: event.pageSize})
    };

    this.router.navigate(['/folder'], {
      queryParams: params
    });
  }

  // search(pageNum = 0, pageSize = this.pageResponse ? this.pageResponse.size : 20) {
  //   const request = this.buildSearchRequest(pageNum, pageSize);

  //   this.router.navigate(['/media'], {
  //     queryParams: request.toUrlParams()
  //   });
  // }

  // buildSearchRequest(
  //   folder,
  //   page = MediaComponent.DEFAULT_PAGE,
  //   pageSize = MediaComponent.DEFAULT_RESULTS_PER_PAGE,
  // ): FolderMediaSearchRequest {

  //   return new FolderMediaSearchRequest(
  //     folder,
  //     this.sortColumn,
  //     this.sortDirection,
  //     this.mediaFilter,
  //     this.onlyShowFavorite,
  //     page,
  //     pageSize
  //   );
  // }

}
