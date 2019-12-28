import { Component, OnInit, ViewChild } from '@angular/core';
import { TagModel } from './tagModel';
import { TagService } from './tag.service';
import { MatTableDataSource, MatPaginator, MatSort, MatDialog, MatTable } from '@angular/material';
import { CreateEditComponent } from './create-edit/create-edit.component';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';
import { AlertService } from '../alert/alert.service';

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

  @ViewChild('table', { static: true }) table: MatTable<TagModel>;
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  @ViewChild(MatSort, { static: true }) sort: MatSort;

  constructor(
    private alertService: AlertService,
    private dialog: MatDialog,
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
      this.dataSource.paginator.firstPage();
    }
  }

  deleteClick(tag: TagModel) {

    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '500px',
      data: {
        title: `Are you sure`,
        message: `${tag.name} will be deleted`
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      if (result) {
        this.tagService.deleteTag(tag.id).subscribe(res => {
          this.alertService.success('Successfully deleted Tag');
          this.tags = this.tags.filter(obj => obj !== tag);
          this.dataSource.data = this.tags; // Push new tag into the existing table
        }, (err) => {
          this.alertService.error('Could not delete Tag');
        });
      }
    });
  }

  editTag(tagToEdit: TagModel) {

    const clonedTag = { ...tagToEdit };

    const dialogRef = this.dialog.open(CreateEditComponent, {
      width: '500px',
      data: clonedTag
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      console.log(result);
      const tag = result;
      // If the tag was edited.
      if (tag) {
        this.tagService.editTag(tag).subscribe(res => {
          const editedTag = res.data;
          this.alertService.success(`Successfully edited Tag!`);

          // Edit the tag already inside the table using the returned data
          tagToEdit.backgroundColor = editedTag.backgroundColor;
          tagToEdit.description = editedTag.description;
          tagToEdit.id = editedTag.id;
          tagToEdit.name = editedTag.name;
          tagToEdit.textColor = editedTag.textColor;
        }, (err) => {
          this.alertService.error(`Could not edit tag successfully: ${err.error.error}`);
        });
      }
    });
  }

  async createTag() {

    const dialogRef = this.dialog.open(CreateEditComponent, {
      width: '500px'
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      console.log(result);
      const tag = result;
      // If the tag was edited.
      if (tag) {
        this.tagService.createNewTag(tag).subscribe(res => {
          this.alertService.success(`Successfully created new Tag!`);
          this.dataSource.data.push(res.data); // Push new tag into the existing table
          this.table.renderRows();
          this.paginator.length++;
        }, (err) => {
          this.alertService.error(`Could not create tag: ${err.error.error}`);
        });
      }
    });
  }

}
