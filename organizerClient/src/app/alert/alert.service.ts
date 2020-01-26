import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material';
import { AlertComponent } from './alert.component';
import { AlertTypes } from './alert-types.enum';

@Injectable({
  providedIn: 'root'
})
export class AlertService {

  readonly GENERIC_FAILURE_MESSAGE = 'An unexpected error occured.';
  constructor(
    private alert: MatSnackBar
  ) { }

  public success(message: string) {
    this.open(message, AlertTypes.SUCCESS, 'alert-success');
  }

  public error(message: string = this.GENERIC_FAILURE_MESSAGE) {
    this.open(message, AlertTypes.ERROR, 'alert-error');
  }

  public warning(message: string) {
    this.open(message, AlertTypes.WARNING, 'alert-warning');
  }

  private open(message: string, type: AlertTypes, panelClass: string | string[] = '') {
    this.alert.openFromComponent(AlertComponent, {
      data: {
        message,
        type
      },
      duration: 5000,
      panelClass
    });
  }
}
