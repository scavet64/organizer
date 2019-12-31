import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource, MatTable, MatPaginator, MatSort, MatDialog } from '@angular/material';
import { ScanLocation } from './scan-location';
import { ScanLocationService } from './scan-location.service';
import { CreateScanLocationDialogComponent } from './create-scan-location-dialog/create-scan-location-dialog.component';
import { AlertService } from '../alert/alert.service';

@Component({
  selector: 'app-scan-locations',
  templateUrl: './scan-locations.component.html',
  styleUrls: ['./scan-locations.component.scss']
})
export class ScanLocationsComponent implements OnInit {

  private scanLocations: ScanLocation[] = [];

  public searchBox: string;
  public mobileView: boolean;

  displayedColumns: string[] = ['Path', 'Lastscan', 'Action'];
  dataSource: MatTableDataSource<ScanLocation>;

  @ViewChild('table', { static: true }) table: MatTable<ScanLocation>;
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  @ViewChild(MatSort, { static: true }) sort: MatSort;

  constructor(
    private alertService: AlertService,
    private dialogRef: MatDialog,
    private scanlocationService: ScanLocationService
  ) { }

  ngOnInit() {
    console.log("init");
    this.scanlocationService.getAllScanLocations().subscribe(res => {
      this.scanLocations = res.data;
      console.log(this.scanLocations);
      this.dataSource = new MatTableDataSource(this.scanLocations);
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
    }, error =>  {
      this.alertService.error(`could not load scan locations ${error.error.error}`);
    });
    //this.sizeCheck();
  }

  createLocation() {
    const dialogRef = this.dialogRef.open(CreateScanLocationDialogComponent, {
      width: '500px'
    });

    dialogRef.afterClosed().subscribe(result => {
      const path = result;
      // If the tag was edited.
      if (path) {
        let scanLoc = new ScanLocation(path);
        this.scanlocationService.createNewScanLocation(scanLoc).subscribe(res => {
          scanLoc = res.data;
          this.alertService.success(`Successfully added new location to scan!`);
        }, (err) => {
          this.alertService.error(`Could not add location: ${err.error.error}`);
        });
      }
    });
  }

  applyFilter(filterValue: string) {
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }
}
