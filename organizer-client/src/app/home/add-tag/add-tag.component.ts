import { Component, OnInit, Input } from '@angular/core';
import { TagService } from 'src/app/tag/tag.service';
import { TagModel } from 'src/app/tag/tagModel';
import { FilterPipe } from 'src/app/tag/filter.pipe';
import { TruncationPipe } from 'src/app/truncation.pipe';
import { MediaFile } from 'src/app/media/media.file';
import { TooltipController, TooltipBox } from 'ionic-tooltips';


@Component({
  selector: 'app-add-tag',
  templateUrl: './add-tag.component.html',
  styleUrls: ['./add-tag.component.scss'],
})
export class AddTagComponent implements OnInit {

  temp2: TooltipBox;
  temp: TooltipController;
  tags: TagModel[];
  searchBox: string;
  @Input() public taggedMedia: MediaFile;

  constructor(
    private tagService: TagService
  ) {}

  ngOnInit() {

    console.log(this.taggedMedia.id);
    this.tagService.getAllTags().subscribe(res => {
      this.tags = res.data;

      this.tags.forEach(tag => {
        if (this.taggedMedia.tags.includes(tag)) {
          tag.selected = true;
        }
      });

    }, (err) => {
      console.log(`Could not get tags`);
    });
  }

}
