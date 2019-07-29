import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Routes, RouterModule } from '@angular/router';

import { IonicModule } from '@ionic/angular';

import { TagPage } from './tag.page';
import { FilterPipe } from './filter.pipe';
import { CoreModule } from '../common/core.module';

const routes: Routes = [
  {
    path: '',
    component: TagPage
  }
];

@NgModule({
  imports: [
    CoreModule,
    CommonModule,
    FormsModule,
    IonicModule,
    RouterModule.forChild(routes)
  ],
  declarations: [TagPage]
})
export class TagPageModule {}
