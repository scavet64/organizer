import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'truncation'
})
export class TruncationPipe implements PipeTransform {

  transform(value: string, limit = 20, completeWords = false, ellipsis = '...') {

    if (value.length < limit) {
      return value;
    }

    if (completeWords) {
      limit = value.substr(0, limit).lastIndexOf(' ');
    }

    return `${value.substr(0, limit)}${ellipsis}`;
  }

}
