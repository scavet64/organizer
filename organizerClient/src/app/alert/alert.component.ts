import { Component, OnInit, Inject } from '@angular/core';
import { AlertTypes } from './alert-types.enum';
import { MAT_SNACK_BAR_DATA } from '@angular/material';

@Component({
  selector: 'app-alert',
  templateUrl: './alert.component.html',
  styleUrls: ['./alert.component.scss']
})
export class AlertComponent implements OnInit {

  AlertTypes = AlertTypes;
  constructor(
    @Inject(MAT_SNACK_BAR_DATA) public data: any
  ) {
   }

  ngOnInit() {
  }

}
