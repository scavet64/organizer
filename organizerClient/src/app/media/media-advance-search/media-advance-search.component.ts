import { Component, Input, OnInit } from '@angular/core';
import { ViewChild } from '@angular/core';
import { MatMenuTrigger } from '@angular/material';
import { TagModel } from 'src/app/tags/tagModel';
import { MediaSortType } from '../media.sort.type';

@Component({
  selector: 'app-media-advance-search',
  templateUrl: './media-advance-search.component.html',
  styleUrls: ['./media-advance-search.component.scss']
})
export class MediaAdvanceSearchComponent implements OnInit {

  @ViewChild('menuTrigger', { static: true }) menuTrigger: MatMenuTrigger;
  @ViewChild('conditionalTrigger', { static: true }) conditionalTrigger: MatMenuTrigger;
  @ViewChild('itemTrigger', { static: true }) itemTrigger: MatMenuTrigger;
  @ViewChild('inputField', { static: true }) inputField: HTMLElement;

  @Input() tags: TagModel[];

  readonly mediaSortOptions: MediaSortType[] = MediaSortType.mediaSortOptions;

  searchBox = '';
  searchParams = [];
  filterOptions = ['Tag'];
  filteredFilterOptions = this.filterOptions;
  conditionalOptions = ['Is', 'Isnt'];

  sortColumn: MediaSortType = MediaSortType.DateAddedSort;
  sortDirection: string;
  acceding = false;

  constructor() { }

  ngOnInit() {
    if (this.acceding) {
      this.sortDirection = 'asc';
    } else {
      this.sortDirection = 'desc';
    }
  }


  inputFocus(event) {
    this.menuTrigger.openMenu();
    console.log(event);
    event.srcElement.focus();
  }

  clickedFilterType(filterType) {
    this.searchParams.push({ key: filterType });
    this.conditionalTrigger.openMenu();
    console.log(this.inputField);
    this.inputField.focus();
  }

  clickedConditional(conditional) {
    this.searchParams[this.searchParams.length - 1].condition = conditional;
    this.itemTrigger.openMenu();
    this.inputField.focus();
  }

  clickedData(data) {
    this.searchParams[this.searchParams.length - 1].data = data.name;
    this.inputField.focus();
  }

  clicked(value) {
    this.searchParams.push({
      key: value,
      condition: 'is',
      data: 'yo'
    });
  }

  dataChanged(event) {
    const value = this.searchBox.toLowerCase();
    this.filteredFilterOptions = this.filterOptions.filter(opt => opt.toLowerCase().indexOf(value) === 0);
  }

  removeSearchKey(searchParam) {
    this.searchParams = this.searchParams.filter(sp => sp !== searchParam);
  }

  clearSearch() {
    this.searchParams = [];
  }

  search() {

  }

  directionFlip() {
    this.acceding = !this.acceding;
  }

  clickedSortingColumn(value) {
    this.sortColumn = value;
  }
}
