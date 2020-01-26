import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FilterPipe } from '../tag/filter.pipe';
import { TruncationPipe } from '../truncation.pipe';
import { MediaFilterPipe } from '../media/media-filter.pipe';
import { PaginationResponse } from './page-response';
import { CardItemComponent } from '../media/card-item/card-item.component';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { MaterialModule } from '../material.module';
import { ListItemComponent } from '../media/list-item/list-item.component';

@NgModule({
  declarations: [
    FilterPipe,
    TruncationPipe,
    MediaFilterPipe,
    CardItemComponent,
    ListItemComponent
  ],
  exports: [
    FilterPipe,
    MediaFilterPipe,
    TruncationPipe,
    CardItemComponent,
    ListItemComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    MaterialModule,
  ]
})
export class CoreModule { }
