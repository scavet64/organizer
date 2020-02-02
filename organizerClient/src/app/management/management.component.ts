import { Component, OnInit } from '@angular/core';
import { MediaService } from '../media/media.service';
import { ManagementService } from './management.service';
import { AlertService } from '../alert/alert.service';

@Component({
  selector: 'app-management',
  templateUrl: './management.component.html',
  styleUrls: ['./management.component.scss']
})
export class ManagementComponent implements OnInit {

  constructor(
    private alertService: AlertService,
    private managementService: ManagementService
  ) { }

  ngOnInit() {
  }

  clearAllScannedMedia() {
    this.managementService.clearScannedMedia().subscribe(res => {
      this.alertService.success(`Successfully cleared all media`);
    }, error => {
      this.alertService.error(`Failed to clear media: ${error.error.error}`);
    });
  }

}