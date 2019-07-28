import { Component, OnInit, Input } from '@angular/core';
import { TagService } from '../tag.service';
import { ToastController, ModalController, NavController } from '@ionic/angular';
import { NgForm } from '@angular/forms';
import { TagModel } from '../tagModel';
import { NavigationExtras, Route, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-create',
  templateUrl: './create-edit.page.html',
  styleUrls: ['./create-edit.page.scss'],
})
export class CreateEditPage implements OnInit {

  //Properties
  mode: string;
  tag: TagModel;

  title: string;
  buttonText: string;

  selectedColor: string;

  constructor(
    private tagService: TagService,
    private toastController: ToastController,
    private modalController: ModalController,
    private navCtrl: NavController,
    private route: ActivatedRoute
    ) { }

  ngOnInit() {}

  ionViewWillEnter() {
    if (this.mode === 'edit') {
      this.title = 'Edit: ';
      this.buttonText = 'edit';
    } else {
      this.title = 'Create: ';
      this.buttonText = 'create';
    }

    console.log(this.tag);
    if (!this.tag) {
      this.tag = new TagModel(
        '',
        undefined,
        this.getRandomColor(),
        this.getRandomColor()
      );
    }
  }

  onSubmit(form: NgForm) {
    // var tag = new TagModel(
    //   this.currentName,
    //   this.description,
    //   this.backgroundColor,
    //   this.textColor
    //   );
    console.log(this.tag);
    console.log(form);
    // this.modalController.dismiss({
    //   data: this.tag
    // });
    this.modalController.dismiss(this.tag);

    // this.tagService.createNewTag(this.tag).subscribe(res => {
    //   console.log(res.data);
    //   this.toastController.create({
    //     message: 'Successfully Create New Tag.',
    //     duration: 5000,
    //     color: 'success'
    //   }).then((toast) => {
    //     toast.present();
    //   });

    //   const navigationExtras: NavigationExtras = {
    //     queryParams: {
    //         createdTag: JSON.stringify(res.data)
    //     }
    //   };
      
    // }, (err) => {
    //   this.toastController.create({
    //     message: `Error creating new tag: ${err.error.error}`,
    //     color: 'danger',
    //     buttons: [
    //       {
    //         text: 'Done',
    //         role: 'cancel',
    //         handler: () => {
    //           console.log('Cancel clicked');
    //         }
    //       }
    //     ]
    //   }).then((toast) => {
    //     toast.present();
    //   });
    // });
  }

  getRandomColor(): string {
    var color = Math.floor(0x1000000 * Math.random()).toString(16);
    return '#' + ('000000' + color).slice(-6);
  }

  cancelClick() {
    this.modalController.dismiss();
  }

}
