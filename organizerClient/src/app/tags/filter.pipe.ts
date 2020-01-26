import { Pipe, PipeTransform } from '@angular/core';
import { TagModel } from './tagModel';

@Pipe({
  name: 'filter'
})
export class FilterPipe implements PipeTransform {

  transform(items: TagModel[], searchText: string): any[] {

    console.log(items);
    if (!items) {
      return [];
    }

    if (!searchText) {
      return items;
    }

    searchText = searchText.toLowerCase();
    return items.filter(it => {
      let include = false;

      if (it.name.toLowerCase().includes(searchText)) {
        include = true;
      }
      if (it.description && it.description.toLowerCase().includes(searchText)) {
        include = true;
      }
      return include;
    });
  }

}
