import { Component, OnInit, Input } from '@angular/core';
import { TagService } from 'src/app/tag/tag.service';
import { TagModel } from 'src/app/tag/tagModel';
import { FilterPipe } from 'src/app/tag/filter.pipe';
import { TruncationPipe } from 'src/app/truncation.pipe';
import { MediaFile } from 'src/app/media/media.file';
import { TooltipController, TooltipBox } from 'ionic-tooltips';
import { PopoverController } from '@ionic/angular';


@Component({
  selector: 'app-add-tag',
  templateUrl: './add-tag.component.html',
  styleUrls: ['./add-tag.component.scss'],
})
export class AddTagComponent implements OnInit {

  temp2: TooltipBox;
  temp: TooltipController;
  searchBox: string;

  @Input() public knownTags: TagModel[];
  //@Input() public taggedMedia: MediaFile;
  // The mediafiles tags
  @Input() public mediasTags: TagModel[];

  constructor(
    private tagService: TagService,
    private popoverController: PopoverController
  ) {
  }

  ngOnInit() {
    console.log(this.mediasTags);
    console.log(this.knownTags);
    this.knownTags.forEach(tag => {
      if (this.mediasTags.find(x => x.id === tag.id)) {
        tag.selected = true;
      } else {
        tag.selected = false;
      }
    });
  }

  tagClicked(tag: TagModel) {
    console.log('testing click');
    if (this.mediasTags.find(x => x.id === tag.id)) {
      this.mediasTags = this.mediasTags.filter(obj => obj.id !== tag.id);
      console.log('tag removed');
    } else {
      this.mediasTags.push(tag);
      console.log('tag added');
    }
  }

  save() {
    console.log(this.mediasTags);
    this.popoverController.dismiss(this.mediasTags);
  }

  cancel() {
    this.popoverController.dismiss();
  }

}
