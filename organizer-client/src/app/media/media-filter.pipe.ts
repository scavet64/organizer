import { Pipe, PipeTransform } from '@angular/core';
import { MediaFile } from './media.file';

@Pipe({
  name: 'mediaFilter'
})
export class MediaFilterPipe implements PipeTransform {

  transform(items: MediaFile[], searchText: string): any[] {

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
      return include;
    });
  }

}
