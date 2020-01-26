import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';

@Component({
  selector: 'app-confirm-dialog',
  templateUrl: './confirm-dialog.component.html',
  styleUrls: ['./confirm-dialog.component.scss']
})
export class ConfirmDialogComponent implements OnInit {

  message: string;
  title: string;
  fileName: string;
  constructor(
    public dialogRef: MatDialogRef<ConfirmDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
    ) {
      this.title = data.title;
      this.message = data.message;
      this.fileName = data.fileName;
    }

  ngOnInit() {
  }

  cancel() {
      this.dialogRef.close(false);
  }

  confirm() {
    this.dialogRef.close(true);
  }

}
