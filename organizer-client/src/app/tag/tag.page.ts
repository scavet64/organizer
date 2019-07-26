import { Component, OnInit } from '@angular/core';
import { TagModel } from './tagModel';

@Component({
  selector: 'app-tag',
  templateUrl: './tag.page.html',
  styleUrls: ['./tag.page.scss'],
})
export class TagPage implements OnInit {

  tags: TagModel[] = [
    new TagModel('testing 1'),
    new TagModel('testing 2'),
    new TagModel('testing 3'),
    new TagModel('testing 4'),
    new TagModel('testing 5'),
    new TagModel('testing 6'),
    new TagModel('testing 7'),
    new TagModel('testing 8')
  ];

  constructor() { }

  ngOnInit() {
  }

}
