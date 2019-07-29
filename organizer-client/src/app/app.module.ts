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
import { CoreModule } from './common/core.module';

@NgModule({
  declarations: [
    CreateEditPage,
    AppComponent
  ],
  entryComponents: [
  ],
  imports: [
    CoreModule,
    BrowserModule,
    FormsModule,
    ColorPickerModule,
    ReactiveFormsModule,
    HttpClientModule,
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
