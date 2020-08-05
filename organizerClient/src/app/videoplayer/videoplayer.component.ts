import { Component, OnInit, Input, Inject, ViewChild, OnDestroy, ElementRef } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material';
import videojs from 'video.js';

@Component({
  selector: 'app-videoplayer',
  templateUrl: './videoplayer.component.html',
  styleUrls: ['./videoplayer.component.scss']
})
export class VideoplayerComponent implements OnInit, OnDestroy {

  // @ViewChild('videoPlayer', { static: true }) videoplayer: any;
  @ViewChild('target', { static: true }) target: ElementRef;
  @Input() options: {
    fluid: boolean,
    aspectRatio: string,
    autoplay: boolean,
    sources: {
      src: string,
      type: string,
    }[],
  };
  player: videojs.Player;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any
  ) { }

  ngOnInit() {
    let volume = localStorage.getItem('volume');
    if (!volume) {
      volume = '0.5';
    }

    let optsToUse;

    if (!this.options) {
      optsToUse = {
        autoplay: false,
        controls: true,
        sources: [
          { src: this.data.rawUrl, type: this.data.mime, default: true, label: 'Raw' },
          { src: this.data.transcodeUrl, type: 'application/x-mpegURL', label: 'Transcode' }
        ]
      };
    }

    console.log(this.data.details);

    // instantiate Video.js
    this.player = videojs(this.target.nativeElement, optsToUse, function onPlayerReady() {
      console.log('onPlayerReady', this);
      console.log(this.currentSrc());
      this.volume(volume);
      this.play();
    });

    const theDuration = this.data.details.duration;
    this.player.duration = () => {
      return theDuration;
    };

    this.player.on('error', function () {
      console.log('yo');
    });
    console.log(this.data.url);
  }

  ngOnDestroy() {
    // Save the users volume level
    localStorage.setItem('volume', this.player.volume());
    // destroy player
    if (this.player) {
      this.player.dispose();
    }
  }

}
