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
}
