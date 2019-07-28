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

  // Properties
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
    this.modalController.dismiss(this.tag);
  }

  getRandomColor(): string {
    const color = Math.floor(0x1000000 * Math.random()).toString(16);
    return '#' + ('000000' + color).slice(-6);
  }

  cancelClick() {
    this.modalController.dismiss();
  }

}
