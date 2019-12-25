import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { TagModel } from '../tagModel';

@Component({
  selector: 'app-create-edit',
  templateUrl: './create-edit.component.html',
  styleUrls: ['./create-edit.component.scss']
})
export class CreateEditComponent implements OnInit {

  title: string;
  buttonText: string;

  selectedColor: string;

  constructor(
    public dialogRef: MatDialogRef<CreateEditComponent>,
    @Inject(MAT_DIALOG_DATA) public tag: TagModel
  ) { }

  ngOnInit() {
    if (this.tag) {
      this.title = 'Edit: ';
      this.buttonText = 'Edit';
    } else {
      this.title = 'Create: ';
      this.buttonText = 'Create';
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

  onNoClick(): void {
    this.dialogRef.close();
  }

  save(): void {
    this.dialogRef.close(this.tag);
  }

  refreshTextColor(event: Event) {
    event.preventDefault();
    event.stopPropagation();
    this.tag.textColor = this.getRandomColor();
  }

  refreshBackgroundColor(event: Event) {
    event.preventDefault();
    event.stopPropagation();
    this.tag.backgroundColor = this.getRandomColor();
  }

  getRandomColor(): string {
    const color = Math.floor(0x1000000 * Math.random()).toString(16);
    return '#' + ('000000' + color).slice(-6);
  }

}
