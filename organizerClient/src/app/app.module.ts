import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { SidenavComponent } from './sidenav/sidenav.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {
  MatButtonModule,
  MatFormFieldModule,
  MatIconModule,
  MatInputModule,
  MatListModule,
  MatSelectModule,
  MatSidenavModule,
  MatTableModule,
  MatGridListModule,
  MatCardModule,
  MatMenuModule,
  MatProgressSpinnerModule,
  MatDividerModule,
  MatChipsModule,
  MatAutocompleteModule,
  MatPaginatorModule,
  MatTooltipModule,
  MatCheckboxModule,
  MatProgressBarModule,
  MatTabsModule,
  MatDatepickerModule,
  MatNativeDateModule,
  MatExpansionModule,
  MatDialogModule,
  MatSnackBarModule
} from "@angular/material";
import { HeaderComponent } from './header/header.component';
import { MainviewComponent } from './mainview/mainview.component';
import { TagsComponent } from './tags/tags.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { ScanLocationsComponent } from './scan-locations/scan-locations.component';
import { MediaComponent } from './media/media.component';
import { HttpClientModule } from '@angular/common/http';
import { FilterPipe } from './tags/filter.pipe';
import { FormsModule } from '@angular/forms';
import { CreateEditComponent } from './tags/create-edit/create-edit.component';
import { ColorPickerModule } from 'ngx-color-picker';

@NgModule({
  declarations: [
    AppComponent,
    SidenavComponent,
    HeaderComponent,
    MainviewComponent,
    TagsComponent,
    DashboardComponent,
    ScanLocationsComponent,
    MediaComponent,
    FilterPipe,
    CreateEditComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    HttpClientModule,
    MatButtonModule,
    MatCardModule,
    MatMenuModule,
    MatProgressSpinnerModule,
    MatDividerModule,
    MatChipsModule,
    MatListModule,
    MatAutocompleteModule,
    MatIconModule,
    MatPaginatorModule,
    MatSelectModule,
    MatTooltipModule,
    MatCheckboxModule,
    MatProgressBarModule,
    MatTabsModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatExpansionModule,
    MatDialogModule,
    MatSidenavModule,
    MatSnackBarModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatTableModule,
    MatGridListModule,
    ColorPickerModule
  ],
  entryComponents: [
    CreateEditComponent
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
