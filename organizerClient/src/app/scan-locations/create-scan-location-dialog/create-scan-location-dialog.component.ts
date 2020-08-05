import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material';
import { BrowseFileSystemService } from '../browse-file-system.service';

@Component({
  selector: 'app-create-scan-location-dialog',
  templateUrl: './create-scan-location-dialog.component.html',
  styleUrls: ['./create-scan-location-dialog.component.scss']
})
export class CreateScanLocationDialogComponent implements OnInit {

  public showHidden = false;  // TODO: Implement better

  public currentPath: string;
  public path: string;

  public breadcrumbs: string[] = [];
  public directories: string[];

  constructor(
    public dialogRef: MatDialogRef<CreateScanLocationDialogComponent>,
    public browseFileSystemService: BrowseFileSystemService
  ) { }

  ngOnInit() {
    this.browseFileSystemService.getRootDirectories().subscribe(res => {
      this.directories = res.data;
    });
  }

  onNoClick(): void {
    this.dialogRef.close(null);
  }

  save(): void {
    this.dialogRef.close(this.path);
  }

  directoryClick(directory: string) {
    if (this.path) {
      this.breadcrumbs.push(this.path);
    }
    this.path = directory;
    this.currentPath = directory;
    this.browseFileSystemService.getChildDirectories(directory, this.showHidden).subscribe(res => {
      this.directories = res.data;
    });
  }

  backClick() {
    // Pop off the top of the stack since we dont need it.
    const toGet = this.breadcrumbs.pop();
    console.log(`back to: ${toGet}`);
    this.path = toGet;
    this.processNavigation(toGet);
  }

  refresh() {
    this.processNavigation(this.currentPath);
  }

  navigateTo() {
    this.processNavigation(this.path);
  }

  processNavigation(path: string) {
    if (path) {
      this.currentPath = path;
      this.browseFileSystemService.getChildDirectories(path, this.showHidden).subscribe(res => {
        this.directories = res.data;
      });
    } else {
      this.currentPath = null;
      this.browseFileSystemService.getRootDirectories().subscribe(res => {
        this.directories = res.data;
      });
    }
  }
}
