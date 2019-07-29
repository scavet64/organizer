import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { RouterModule } from '@angular/router';

import { HomePage } from './home.page';
import { ResourceTreeComponent } from './resource-tree/resource-tree.component';
import { CoreModule } from '../common/core.module';
import { TooltipsModule } from 'ionic-tooltips';

@NgModule({
  imports: [
    CommonModule,
    TooltipsModule,
    CoreModule,
    FormsModule,
    IonicModule,
    RouterModule.forChild([
      {
        path: '',
        component: HomePage
      }
    ])
  ],
  declarations: [HomePage, ResourceTreeComponent]
})
export class HomePageModule {}
