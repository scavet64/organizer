export class MediaSortType {

  public static readonly advanceSearchStateKey = 'mediaAdvanceSearch';
  // public static readonly IdSort = new MediaSortType('ID', 'id');
  public static readonly NameSort = new MediaSortType('Name', 'name');
  public static readonly DateAddedSort = new MediaSortType('Date Added', 'dateAdded');
  public static readonly DateCreatedSort = new MediaSortType('Date Created', 'dateCreated');
  public static readonly DateModifiedSort = new MediaSortType('Date Modified', 'lastModified');
  public static readonly FileSizeSort = new MediaSortType('File Size', 'size');
  public static readonly ViewsSort = new MediaSortType('Views', 'views');
  public static readonly RatingSort = new MediaSortType('Rating', 'rating');
  public static readonly isFavorite = new MediaSortType('Favorites', 'isFavorite');
  public static readonly mediaSortOptions = [
    // MediaSortType.IdSort,
    MediaSortType.NameSort,
    MediaSortType.DateAddedSort,
    MediaSortType.DateCreatedSort,
    MediaSortType.DateModifiedSort,
    MediaSortType.FileSizeSort,
    MediaSortType.ViewsSort,
    MediaSortType.RatingSort,
    MediaSortType.isFavorite
  ];

  public displayName: string;
  public value: string;

  constructor(displayName: string, value: string) {
    this.displayName = displayName;
    this.value = value;
  }
}
