import { TagModel } from '../tag/tagModel';

export interface MediaFile {
    id: number;
    hash: string;
    name: string;
    size: BigInteger;
    path: string;
    tags: TagModel[];
    mimetype: string;
}
