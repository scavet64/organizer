import { TagModel } from '../tag/tagModel';
import { HttpParams } from '@angular/common/http';
import { PageParams } from '../common/PageParams';
import { PageRequest } from '../common/PageRequest';

export class MediaSearchRequest extends PageRequest {
    name: string;
    tags: string[];

    constructor(pageNumber: number, name: string, tags: TagModel[]) {
        super(pageNumber);
        this.name = name;
        this.tags = new Array();

        if (tags != null) {
            tags.forEach(element => {
                this.tags.push(element.name);
            });
        }

    }

    toHttpParams(): HttpParams {
        let params = super.toHttpParams();
        if (this.name && this.name !== '') {
            params = params.set('name', `like:${this.name}`);
        }

        if (this.tags.length > 0) {
            params = params.set('tags', this.tags.toString());
        }
        return params;
    }
}
