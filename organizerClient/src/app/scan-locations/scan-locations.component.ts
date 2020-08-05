import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource, MatTable, MatPaginator, MatSort, MatDialog } from '@angular/material';
import { ScanLocation } from './scan-location';
import { ScanLocationService } from './scan-location.service';
import { CreateScanLocationDialogComponent } from './create-scan-location-dialog/create-scan-location-dialog.component';
import { AlertService } from '../alert/alert.service';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-scan-locations',
  templateUrl: './scan-locations.component.html',
  styleUrls: ['./scan-locations.component.scss']
})
export class ScanLocationsComponent implements OnInit {

  private scanLocations: ScanLocation[] = [];

  public searchBox: string;
  public mobileView: boolean;

  displayedColumns: string[] = ['path', 'lastscan', 'action'];
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
    this.scanlocationService.getAllScanLocations().subscribe(res => {
      this.scanLocations = res.data;
      console.log(this.scanLocations);
      this.dataSource = new MatTableDataSource(this.scanLocations);
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
    }, error => {
      this.alertService.error(`could not load scan locations ${error.error.error}`);
    });
    //this.sizeCheck();
  }

  createLocation() {
    const dialogRef = this.dialogRef.open(CreateScanLocationDialogComponent, {
      width: '750px'
    });

    dialogRef.afterClosed().subscribe(result => {
      const path = result;
      // If the tag was edited.
      if (path) {
        let scanLoc = new ScanLocation(path);
        this.scanlocationService.createNewScanLocation(scanLoc).subscribe(res => {
          scanLoc = res.data;
          this.scanLocations.push(scanLoc);
          this.dataSource.data = this.scanLocations;
          this.alertService.success(`Successfully added new location to scan!`);
        }, (err) => {
          this.alertService.error(`Could not add location: ${err.error.error}`);
        });
      }
    });
  }

  deleteScanLocation(scanLocation: ScanLocation) {
    const dialogRef = this.dialogRef.open(ConfirmDialogComponent, {
      width: '500px',
      data: {
        title: `Are you sure?`,
        message: `'${scanLocation.path}' will no longer be scanned. This will remove all the media scanned from this location.`
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.scanlocationService.deleteScanLocation(scanLocation.id).subscribe(res => {
          this.alertService.success('Successfully removed Scan Location');
          this.scanLocations = this.scanLocations.filter(obj => obj !== scanLocation);
          this.dataSource.data = this.scanLocations;
        }, (err) => {
          this.alertService.error(`Could not remove Scan Location: ${err.error.error}`);
        });
      }
    });
  }

  scanNow(scanLocation: ScanLocation) {
    this.scanlocationService.initiateScan(scanLocation.id).subscribe(res => {
      this.alertService.success(res.data);
    }, (err) => {
      this.alertService.error(`Failed starting scan: ${err.error.error}`);
    });
  }

  applyFilter(filterValue: string) {
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }
}
