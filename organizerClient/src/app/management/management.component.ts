import { Component, Input, OnInit } from '@angular/core';
import { MediaService } from '../media/media.service';
import { ManagementService } from './management.service';
import { AlertService } from '../alert/alert.service';
import { ViewChild } from '@angular/core';
import { MatMenuTrigger } from '@angular/material';
import { TagService } from '../tags/tag.service';

@Component({
  selector: 'app-management',
  templateUrl: './management.component.html',
  styleUrls: ['./management.component.scss']
})
export class ManagementComponent implements OnInit {

  tags: any = [];

  constructor(
    private alertService: AlertService,
    private managementService: ManagementService,
    private tagService: TagService
  ) { }

  ngOnInit() {
    this.tagService.getAllTags().subscribe(res => {
      this.tags = res.data;
    });
  }

  clearAllScannedMedia() {
    this.managementService.clearScannedMedia().subscribe(res => {
      this.alertService.success(`Successfully cleared all media`);
    }, error => {
      this.alertService.error(`Failed to clear media: ${error.error.error}`);
    });
  }
}
