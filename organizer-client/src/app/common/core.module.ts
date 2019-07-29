import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FilterPipe } from '../tag/filter.pipe';
import { TruncationPipe } from '../truncation.pipe';

@NgModule({
  declarations: [
    FilterPipe,
    TruncationPipe
  ],
  exports: [
    FilterPipe,
    TruncationPipe
  ],
  imports: [
    CommonModule
  ]
})
export class CoreModule { }
