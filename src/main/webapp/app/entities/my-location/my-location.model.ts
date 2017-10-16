import { BaseEntity } from './../../shared';

export class MyLocation implements BaseEntity {
    constructor(
        public id?: number,
        public realLocationName?: string,
        public myLocationName?: string,
        public lati?: string,
        public longi?: string,
        public appUser?: BaseEntity,
    ) {
    }
}
