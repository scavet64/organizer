import { Component, OnInit, ViewChild } from '@angular/core';
import { TagModel } from './tagModel';
import { TagService } from './tag.service';
import { MatTableDataSource, MatPaginator, MatSort } from '@angular/material';

@Component({
  selector: 'app-tags',
  templateUrl: './tags.component.html',
  styleUrls: ['./tags.component.scss']
})
export class TagsComponent implements OnInit {

  displayedColumns: string[] = ['Tag', 'Description'];
  searchBox: string;
  tags: TagModel[];
  dataSource: MatTableDataSource<TagModel>;

  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  @ViewChild(MatSort, {static: true}) sort: MatSort;

  constructor(
    private tagService: TagService
    ) { }

  ngOnInit() {
    this.tagService.getAllTags().subscribe(res => {
      this.tags = res.data;
      console.log(this.tags);
      this.dataSource = new MatTableDataSource(this.tags);
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
    });
  }

  applyFilter(filterValue: string) {
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      console.log('page');
      this.dataSource.paginator.firstPage();
    }
  }

  deleteClick(tag: TagModel) {
    // this.alertController.create(
    //   {
    //     message: `Are you sure you want to delete "${tag.name}"? This action is irreversible.`,
    //     buttons: [
    //       {
    //         text: 'No',
    //         role: 'cancel',
    //         cssClass: 'alert',
    //         handler: (blah) => {
    //           console.log('Confirm Cancel: blah');
    //         }
    //       }, {
    //         text: 'Yes',
    //         handler: () => {
    //           console.log('Confirm Okay');
    //           this.tagService.deleteTag(tag.id).subscribe(res => {
    //             this.toastingService.showSuccessToast('Successfully deleted Tag');
    //             this.tags = this.tags.filter(obj => obj !== tag);
    //           }, (err) => {
    //             this.toastingService.showPersistentErrorToast('Could not delete Tag');
    //           });
    //         }
    //       }
    //     ]
    //   }
    // ).then((alert) => {
    //   alert.present();
    // });
  }

  async editTag(tagToEdit: TagModel) {

    // const clonedTag = {...tagToEdit};

    // const modal = await this.modalController.create({
    //   component: CreateEditPage,
    //   cssClass: 'my-custom-modal-css',
    //   componentProps: {
    //     tag: clonedTag,
    //     mode: 'edit'
    //   }
    // });
    // await modal.present();

    // modal.onDidDismiss()
    //   .then((data) => {
    //     const tag = data.data;
    //     // If the tag was edited.
    //     if (tag) {
    //       this.tagService.editTag(tag).subscribe(res => {
    //         const editedTag = res.data;
    //         this.toastingService.showSuccessToast(`Successfully edited Tag!`);

    //         // Edit the tag already inside the table using the returned data
    //         tagToEdit.backgroundColor = editedTag.backgroundColor;
    //         tagToEdit.name = editedTag.nam;
    //         tagToEdit.id = editedTag.id;
    //         tagToEdit.name = editedTag.name;
    //         tagToEdit.textColor = editedTag.textColor;
    //       }, (err) => {
    //         this.toastingService.showPersistentErrorToast(`Could not edit tag successfully: ${err.error.error}`);
    //       });
    //     }
    //   });
  }

  async createTag() {
    // const modal = await this.modalController.create({
    //   component: CreateEditPage,
    //   cssClass: 'my-custom-modal-css',
    //   componentProps: {
    //     mode: 'create'
    //   }
    // });
    // await modal.present();

    // modal.onDidDismiss()
    //   .then((data) => {
    //     const tag = data.data;
    //     if (tag) {
    //       this.tagService.createNewTag(tag).subscribe(res => {
    //         this.toastingService.showSuccessToast('Successfully created new tag!');
    //         this.tags.push(res.data); // Push new tag into the existing table
    //       }, (err) => {
    //         this.toastingService.showPersistentErrorToast(`Could not create tag: ${err.error.error}`);
    //       });
    //     }
    // });
  }

}
