export class ScanLocation {
  id: number;
  path: string;
  lastScan: Date;

  constructor(path: string) {
    this.path = path;
  }
}
