import { Injectable } from '@angular/core';
import { ToastController } from '@ionic/angular';

@Injectable({
  providedIn: 'root'
})
export class ToastingService {

  constructor(
    private toastController: ToastController
  ) { }

    public showSuccessToast(displayMessage: string) {
      this.showToast(displayMessage, 'success');
    }

    public showErrorToast(displayMessage: string) {
      this.showToast(displayMessage, 'danger');
    }

    public showWarningToast(displayMessage: string) {
      this.showToast(displayMessage, 'warning');
    }

    private showToast(displayMessage: string, displayColor: string) {
      this.toastController.create({
        message: displayMessage,
        duration: 5000,
        color: displayColor
      }).then((toast) => {
        toast.present();
      });
    }

    public showPersistentErrorToast(displayMessage: string) {
      this.toastController.create({
        message: displayMessage,
        color: 'danger',
        buttons: [
          {
            text: 'Okay',
            role: 'cancel',
            handler: () => {
              console.log('Cancel clicked');
            }
          }
        ]
      }).then((toast) => {
        toast.present();
      });
    }

}
