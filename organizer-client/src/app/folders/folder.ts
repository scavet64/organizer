import { MediaFile } from '../media/media.file';

export class Folder {
    path: string;
    parentPath: string;
    folderName: string;
    name: string;
    folders: Folder[];
    files: MediaFile[];
    visibleInTree: boolean = false;
}
