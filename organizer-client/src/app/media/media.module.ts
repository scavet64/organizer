import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Routes, RouterModule } from '@angular/router';

import { IonicModule } from '@ionic/angular';

import { MediaPage } from './media.page';
import { CoreModule } from '../common/core.module';

const routes: Routes = [
  {
    path: '',
    component: MediaPage
  }
];

@NgModule({
  imports: [
    CommonModule,
    CoreModule,
    FormsModule,
    IonicModule,
    RouterModule.forChild(routes)
  ],
  declarations: [MediaPage]
})
export class MediaPageModule {}