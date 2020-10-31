import { Component, OnInit, HostListener } from '@angular/core';
import { Constants } from '../common/constants';

@Component({
  selector: 'app-mainview',
  templateUrl: './mainview.component.html',
  styleUrls: ['./mainview.component.scss']
})
export class MainviewComponent implements OnInit {

  private MIN_WIDTH = Constants.MIN_WIDTH;
  private innerWidth: number;
  isOpened: boolean;
  isMobile: boolean;

  sideNavMode: string;
  DESKTOP_SIDENAV = 'side';
  MOBILE_SIDENAV = 'over';

  constructor() {}

  @HostListener('window:resize', ['$event'])
  onResize(event) {
    this.sizeCheck();
  }

  ngOnInit() {
    this.sizeCheck();
  }

  sizeCheck() {
    this.innerWidth = window.innerWidth;
    if (this.innerWidth < this.MIN_WIDTH) {
      this.sideNavMode = this.MOBILE_SIDENAV;
      this.isMobile = true;
      this.isOpened = false;
    } else {
      this.sideNavMode = this.DESKTOP_SIDENAV;
      this.isMobile = false;
      this.isOpened = true;
    }
  }
}
