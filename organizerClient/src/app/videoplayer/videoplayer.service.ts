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

    this.mediaFileService.getMediaDetails(file).subscribe(res => {
      console.log(res);
      const dialogRef = this.dialog.open(VideoplayerComponent, {
        width: '80vw',
        height: '90vh',
        panelClass: 'videomodel',
        data: {
          rawUrl: this.mediaFileService.getVideoStreamURL(file),
          transcodeUrl: this.mediaFileService.getVideoStreamURLTranscode(file),
          mime: file.mimetype,
          details: res.data
        }
      });

      this.mediaFileService.addView(file.id).subscribe(res => {
        console.log(res.data);
        file.views++;
      });

      dialogRef.afterClosed().subscribe(result => {
        console.log("closed video");
      });
    });
  }

}
