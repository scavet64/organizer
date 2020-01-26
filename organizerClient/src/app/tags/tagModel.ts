export class TagModel {
    id: number;
    name: string;
    description: string;
    backgroundColor: string;
    textColor: string;
    selected?: boolean;

    constructor(name: string, description: string, backgroundColor: string, textColor: string) {
        this.name = name;
        this.description = description;
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
    }
}
