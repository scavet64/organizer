import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { RouteReuseStrategy } from '@angular/router';

import { IonicModule, IonicRouteStrategy } from '@ionic/angular';
import { SplashScreen } from '@ionic-native/splash-screen/ngx';
import { StatusBar } from '@ionic-native/status-bar/ngx';

import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { HttpClientModule } from '@angular/common/http';
import { ResourceTreeComponent } from './home/resource-tree/resource-tree.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ColorPickerModule } from 'ngx-color-picker';
import { CreateEditPage } from './tag/create-edit/create-edit.page';
import { SweetAlert2Module } from '@sweetalert2/ngx-sweetalert2';
import { MediaTagsComponent } from './media-tags/media-tags.component';
import { HomePage } from './home/home.page';
import { FilterPipe } from './tag/filter.pipe';
import { CoreModule } from './common/core.module';
import { TruncationPipe } from './truncation.pipe';
import { TooltipsModule } from 'ionic-tooltips';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

@NgModule({
  declarations: [
    CreateEditPage,
    AppComponent,
    MediaTagsComponent
  ],
  entryComponents: [
    MediaTagsComponent
  ],
  imports: [
    CoreModule,
    BrowserModule,
    FormsModule,
    ColorPickerModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    HttpClientModule,
    TooltipsModule.forRoot(),
    IonicModule.forRoot(),
    AppRoutingModule,
    SweetAlert2Module
  ],
  providers: [
    StatusBar,
    SplashScreen,
    { provide: RouteReuseStrategy, useClass: IonicRouteStrategy }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
