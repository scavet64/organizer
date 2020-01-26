import { Component, OnInit, Input } from '@angular/core';
import { MediaServiceService } from '../media-service.service';
import { MediaTagsComponent } from 'src/app/media-tags/media-tags.component';
import { PopoverController } from '@ionic/angular';
import { MediaFile } from '../media.file';
import { ToastingService } from 'src/app/toasting.service';
import { MediaComponentService } from '../media-component.service';

@Component({
  selector: 'app-card-item',
  templateUrl: './card-item.component.html',
  styleUrls: ['./card-item.component.scss'],
})
export class CardItemComponent implements OnInit {

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
