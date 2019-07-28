import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Routes, RouterModule } from '@angular/router';

import { IonicModule } from '@ionic/angular';

import { TagPage } from './tag.page';
import { FilterPipe } from './filter.pipe';

const routes: Routes = [
  {
    path: '',
    component: TagPage
  }
];

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    RouterModule.forChild(routes)
  ],
  declarations: [TagPage, FilterPipe]
})
export class TagPageModule {}
