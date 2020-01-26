export class PaginationResponse<Type> {
    content: Type[];
    empty: boolean;
    first: boolean;
    last: false;
    number: number;
    numberOfElements: number;
    pageable: any;
    size: number;
    sort: any;
    totalElements: number;
    totalPages: number;
}
