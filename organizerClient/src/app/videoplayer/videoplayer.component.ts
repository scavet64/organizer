import { Component, OnInit, Input, Inject, ViewChild, OnDestroy } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material';

@Component({
  selector: 'app-videoplayer',
  templateUrl: './videoplayer.component.html',
  styleUrls: ['./videoplayer.component.scss']
})
export class VideoplayerComponent implements OnInit, OnDestroy {

  @ViewChild('videoPlayer', {static: true}) videoplayer: any;

  constructor(
    @Inject(MAT_DIALOG_DATA) public url: string
  ) { }

  ngOnInit() {
    let volume = localStorage.getItem('volume');
    if (!volume) {
      volume = '0.5';
    }
    this.videoplayer.nativeElement.volume = volume;
  }

  ngOnDestroy() {
    localStorage.setItem('volume', this.videoplayer.nativeElement.volume);
  }

}
