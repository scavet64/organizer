import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material';

@Component({
  selector: 'app-create-scan-location-dialog',
  templateUrl: './create-scan-location-dialog.component.html',
  styleUrls: ['./create-scan-location-dialog.component.scss']
})
export class CreateScanLocationDialogComponent implements OnInit {

  public path: string;

  constructor(
    public dialogRef: MatDialogRef<CreateScanLocationDialogComponent>
  ) { }

  ngOnInit() {
  }

  onNoClick(): void {
    this.dialogRef.close(null);
  }

  save(): void {
    this.dialogRef.close(this.path);
  }

}
