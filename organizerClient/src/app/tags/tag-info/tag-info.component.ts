import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { TagModel } from '../tagModel';

@Component({
  selector: 'app-tag-info',
  templateUrl: './tag-info.component.html',
  styleUrls: ['./tag-info.component.scss']
})
export class TagInfoComponent implements OnInit {

  tag: TagModel;

  constructor(
    public dialogRef: MatDialogRef<TagInfoComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    this.tag = this.data;
   }

  ngOnInit() {
  }

}
