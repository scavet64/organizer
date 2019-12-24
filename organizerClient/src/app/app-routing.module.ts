import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { SidenavComponent } from './sidenav/sidenav.component';
import { MainviewComponent } from './mainview/mainview.component';
import { TagsComponent } from './tags/tags.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { ScanLocationsComponent } from './scan-locations/scan-locations.component';
import { MediaComponent } from './media/media.component';


const routes: Routes = [
  {path: '', component: MainviewComponent, children: [
    {path: 'dashboard', component: DashboardComponent},
    {path: 'tags', component: TagsComponent},
    {path: 'scanlocations', component: ScanLocationsComponent},
    {path: 'media', component: MediaComponent}
  ]}

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
