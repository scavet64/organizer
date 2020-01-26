import { Component, OnInit, Input } from '@angular/core';
import { MediaFile } from '../media.file';
import { MediaServiceService } from '../media-service.service';

@Component({
  selector: 'app-videoplayer',
  templateUrl: './videoplayer.component.html',
  styleUrls: ['./videoplayer.component.scss'],
})
export class VideoplayerComponent implements OnInit {

  @Input() public mediaFile: MediaFile;

  constructor(
    private mediaFileService: MediaServiceService
  ) { }

  ngOnInit() {}

  public getVideoUrl(file: MediaFile): string {
    return this.mediaFileService.getVideoStreamURL(file);
  }

}
