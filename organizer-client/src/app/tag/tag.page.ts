import { Component, OnInit } from '@angular/core';
import { TagModel } from './tagModel';
import { TagService } from './tag.service';
import { ModalController, NavController, ToastController, AlertController } from '@ionic/angular';
import { ActivatedRoute, NavigationExtras } from "@angular/router";
import { CreateEditPage } from './create-edit/create-edit.page';
import { ToastingService } from '../toasting.service';

@Component({
  selector: 'app-tag',
  templateUrl: './tag.page.html',
  styleUrls: ['./tag.page.scss'],
})
export class TagPage {

  searchBox: string;
  tags: TagModel[];

  constructor(
    private route: ActivatedRoute,
    private alertController: AlertController,
    private tagService: TagService,
    private modalController: ModalController,
    private navCtrl: NavController,
    private toastingService: ToastingService
  ) { }

  ionViewWillEnter() {
    this.tagService.getAllTags().subscribe(res => {
      this.tags = res.data;
    });
  }

  // Maybe just use a modal
  createNew() {
    const navigationExtras: NavigationExtras = {
      queryParams: {
        m: 'create'
      }
    };
    this.navCtrl.navigateForward(['tag/modification'], navigationExtras);
  }

  // Maybe just use a modal
  editClick(tag: TagModel) {
    const navigationExtras: NavigationExtras = {
      queryParams: {
        m: 'edit'
      }
    };
    this.navCtrl.navigateForward(['tag/modification'], navigationExtras);
  }

  deleteClick(tag: TagModel) {

    this.alertController.create(
      {
        header: 'Are you sure?',
        message: `Are you sure you want to delete ${tag.name}? This action is not reversible.`,
        buttons: [
          {
            text: 'No',
            role: 'cancel',
            cssClass: 'secondary',
            handler: (blah) => {
              console.log('Confirm Cancel: blah');
            }
          }, {
            text: 'Yes',
            handler: () => {
              console.log('Confirm Okay');
              this.tagService.deleteTag(tag.id).subscribe(res => {
                this.toastingService.showSuccessToast('Successfully deleted Tag');
                this.tags = this.tags.filter(obj => obj !== tag);
              }, (err) => {
                this.toastingService.showPersistentErrorToast('Could not delete Tag');
              });
            }
          }
        ]
      }
    ).then((alert) => {
      alert.present();
    });
  }

  // async presentModal() {
  //   const modal = await this.modalController.create({
  //     component: NewtagComponent,
  //     cssClass: 'my-custom-modal-css'
  //   });
  //   await modal.present();

  //   modal.onDidDismiss()
  //     .then((data) => {
  //       const tag = data['data']; // Here's your selected user!
  //       if (tag !== undefined) {
  //         this.tags.push(tag);
  //       }
  //   });
  //}

}
