import { TagModel } from '../tags/tagModel';
import { StoredFile } from './stored.file';

export interface MediaFile {
    id: number;
    hash: string;
    name: string;
    size: BigInteger;
    path: string;
    tags: TagModel[];
    mimetype: string;
    thumbnail: StoredFile;
    views: number;
    dateCreated: Date;
    dateAdded: Date;
    lastModified: Date;
    isFavorite: boolean;
    isSelected: boolean;  // This is used in media page to determine if is selected when editing multiple media tags
}
