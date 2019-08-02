import { MediaFile } from '../media/media.file';

export class Folder {
    id: number;
    path: string;
    parentPath: string;
    folderName: string;
    name: string;
    folders: Folder[];
    files: MediaFile[];
    visibleInTree: boolean = false;
}
