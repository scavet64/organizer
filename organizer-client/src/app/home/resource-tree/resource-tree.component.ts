import { Component, Input, EventEmitter, Output } from '@angular/core';
import { Folder } from 'src/app/folders/folder';

@Component({
  selector: 'app-resource-tree',
  templateUrl: './resource-tree.component.html',
  styleUrls: ['./resource-tree.component.scss'],
})
export class ResourceTreeComponent {
  @Input() TreeData: Folder[];
  @Input() hasCheckbox = true;
  @Output() selected = new EventEmitter();

  constructor() { }

  toggleChildren(node: any) {
      node.visibleInTree = !node.visibleInTree;
  }

}
