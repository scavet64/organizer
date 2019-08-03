import { Component, OnInit, Input } from '@angular/core';
import { MediaComponentService } from '../media-component.service';
import { MediaFile } from '../media.file';

@Component({
  selector: 'app-list-item',
  templateUrl: './list-item.component.html',
  styleUrls: ['./list-item.component.scss'],
})
export class ListItemComponent implements OnInit {

  @Input() file;
  @Input() knownTags;

  constructor(
    private mediaComponentService: MediaComponentService
  ) { }

  ngOnInit() {}

  async presentPopover(ev: any, mediaFile: MediaFile) {
    this.mediaComponentService.presentPopover(ev, mediaFile, this.knownTags);
  }

  public getVideoUrl(file: MediaFile) {
    return this.mediaComponentService.getVideoUrl(file);
  }

  async showFullscreen(toShow: MediaFile) {
    this.mediaComponentService.showFullscreen(toShow);
  }

}
