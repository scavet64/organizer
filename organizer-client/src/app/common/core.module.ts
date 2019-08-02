import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FilterPipe } from '../tag/filter.pipe';
import { TruncationPipe } from '../truncation.pipe';
import { MediaFilterPipe } from '../media/media-filter.pipe';

@NgModule({
  declarations: [
    FilterPipe,
    TruncationPipe,
    MediaFilterPipe
  ],
  exports: [
    FilterPipe,
    MediaFilterPipe,
    TruncationPipe
  ],
  imports: [
    CommonModule
  ]
})
export class CoreModule { }
