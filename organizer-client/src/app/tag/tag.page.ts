import { Component, OnInit } from '@angular/core';
import { TagModel } from './tagModel';
import { TagService } from './tag.service';
import { ModalController, NavController, ToastController, AlertController } from '@ionic/angular';
import { ActivatedRoute, NavigationExtras } from "@angular/router";
import { CreateEditPage } from './create-edit/create-edit.page';
import { ToastingService } from '../toasting.service';
import { FilterPipe} from './filter.pipe';

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

  deleteClick(tag: TagModel) {
    this.alertController.create(
      {
        message: `Are you sure you want to delete "${tag.name}"? This action is irreversible.`,
        buttons: [
          {
            text: 'No',
            role: 'cancel',
            cssClass: 'alert',
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

  async editTag(tagToEdit: TagModel) {

    const clonedTag = {...tagToEdit};

    const modal = await this.modalController.create({
      component: CreateEditPage,
      cssClass: 'my-custom-modal-css',
      componentProps: {
        tag: clonedTag,
        mode: 'edit'
      }
    });
    await modal.present();

    modal.onDidDismiss()
      .then((data) => {
        const tag = data.data;
        // If the tag was edited.
        if (tag) {
          this.tagService.editTag(tag).subscribe(res => {
            const editedTag = res.data;
            this.toastingService.showSuccessToast(`Successfully edited Tag!`);

            // Edit the tag already inside the table using the returned data
            tagToEdit.backgroundColor = editedTag.backgroundColor;
            tagToEdit.name = editedTag.nam;
            tagToEdit.id = editedTag.id;
            tagToEdit.name = editedTag.name;
            tagToEdit.textColor = editedTag.textColor;
          }, (err) => {
            this.toastingService.showPersistentErrorToast(`Could not edit tag successfully: ${err.error.error}`);
          });
        }
      });
  }

  async createTag() {
    const modal = await this.modalController.create({
      component: CreateEditPage,
      cssClass: 'my-custom-modal-css',
      componentProps: {
        mode: 'create'
      }
    });
    await modal.present();

    modal.onDidDismiss()
      .then((data) => {
        const tag = data.data;
        if (tag) {
          this.tagService.createNewTag(tag).subscribe(res => {
            this.toastingService.showSuccessToast('Successfully created new tag!');
            this.tags.push(res.data); // Push new tag into the existing table
          }, (err) => {
            this.toastingService.showPersistentErrorToast(`Could not create tag: ${err.error.error}`);
          });
        }
    });
  }

}
