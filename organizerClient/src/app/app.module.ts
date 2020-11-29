import { BrowserModule } from '@angular/platform-browser';
import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

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
  MatSortModule,
  MatButtonToggleModule,
  MatSlideToggleModule
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
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { CreateScanLocationDialogComponent } from './scan-locations/create-scan-location-dialog/create-scan-location-dialog.component';
import { FolderComponent } from './folder/folder.component';
import { VideoplayerComponent } from './videoplayer/videoplayer.component';
import { ManagementComponent } from './management/management.component';
import { MediaInfoComponent } from './media/media-info/media-info.component';
import { DataSizePipe } from './common/data-size.pipe';
import { TagInfoComponent } from './tags/tag-info/tag-info.component';
import { MediaListGridItemComponent } from './media/media-list-grid-item/media-list-grid-item.component';
import { VjsPlayerComponent } from './vjs-player/vjs-player.component';
import { MediaAdvanceSearchComponent } from './media/media-advance-search/media-advance-search.component';
import { MatRippleModule } from '@angular/material/core';
import { MediaListComponent } from './media/media-list/media-list.component';

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
    VideoplayerComponent,
    ManagementComponent,
    MediaInfoComponent,
    DataSizePipe,
    TagInfoComponent,
    MediaListGridItemComponent,
    VjsPlayerComponent,
    MediaAdvanceSearchComponent,
    MediaListComponent
  ],
  imports: [
    AppRoutingModule,
    BrowserAnimationsModule,
    BrowserModule,
    ColorPickerModule,
    HttpClientModule,
    MatButtonModule,
    MatButtonToggleModule,
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
    MatSlideToggleModule,
    MatSnackBarModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatTableModule,
    MatGridListModule,
    NgbModule,
    MatRippleModule,
    ReactiveFormsModule,
    MatSortModule
  ],
  entryComponents: [
    AlertComponent,
    ConfirmDialogComponent,
    CreateEditComponent,
    CreateScanLocationDialogComponent,
    MediaInfoComponent,
    MediaTagsComponent,
    TagInfoComponent,
    VideoplayerComponent
  ],
  providers: [],
  bootstrap: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AppModule { }
