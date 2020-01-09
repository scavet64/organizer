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
  MatSnackBarModule,
  MatSortModule
} from "@angular/material";
import { HeaderComponent } from './header/header.component';
import { MainviewComponent } from './mainview/mainview.component';
import { TagsComponent } from './tags/tags.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { ScanLocationsComponent } from './scan-locations/scan-locations.component';
import { MediaComponent } from './media/media.component';
import { HttpClientModule } from '@angular/common/http';
import { FilterPipe } from './tags/filter.pipe';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CreateEditComponent } from './tags/create-edit/create-edit.component';
import { ColorPickerModule } from 'ngx-color-picker';
import { ConfirmDialogComponent } from './confirm-dialog/confirm-dialog.component';
import { AlertComponent } from './alert/alert.component';
import { MediaListItemComponent } from './media/media-list-item/media-list-item.component';
import { TruncationPipe } from './common/truncation.pipe';
import { MediaTagsComponent } from './media/media-tags/media-tags.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import { CreateScanLocationDialogComponent } from './scan-locations/create-scan-location-dialog/create-scan-location-dialog.component';
import { FolderComponent } from './folder/folder.component';
import { VideoplayerComponent } from './videoplayer/videoplayer.component';

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
    CreateEditComponent,
    ConfirmDialogComponent,
    AlertComponent,
    MediaListItemComponent,
    TruncationPipe,
    MediaTagsComponent,
    CreateScanLocationDialogComponent,
    FolderComponent,
    VideoplayerComponent
  ],
  imports: [
    AppRoutingModule,
    BrowserAnimationsModule,
    BrowserModule,
    ColorPickerModule,
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
    NgbModule,
    ReactiveFormsModule,
    MatSortModule
  ],
  entryComponents: [
    AlertComponent,
    ConfirmDialogComponent,
    CreateEditComponent,
    CreateScanLocationDialogComponent,
    MediaTagsComponent,
    VideoplayerComponent
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
