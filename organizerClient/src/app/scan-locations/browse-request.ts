export class BrowseRequest {
  path: string;
  showHidden: boolean;

  constructor(path: string, showHidden: boolean) {
    this.path = path;
    this.showHidden = showHidden;
  }
}
