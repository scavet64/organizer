import { Injectable } from '@angular/core';
import { MediaService } from '../media/media.service';
import { MediaFile } from '../media/media.file';
import { MatDialog } from '@angular/material';
import { VideoplayerComponent } from './videoplayer.component';

@Injectable({
  providedIn: 'root'
})
export class VideoplayerService {

  constructor(
    private mediaFileService: MediaService,
    private dialog: MatDialog
  ) { }

  public showVideo(file: MediaFile) {
    const dialogRef = this.dialog.open(VideoplayerComponent, {
      width: '80%',
      maxHeight: '90%',
      panelClass: 'videomodel',
      data: this.mediaFileService.getVideoStreamURL(file)
    });

    this.mediaFileService.addView(file.id).subscribe(res => {
      console.log(res.data);
      file.views++;
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log("closed video");
    });
  }

}
