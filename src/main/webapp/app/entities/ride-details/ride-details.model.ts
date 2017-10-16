import { BaseEntity } from './../../shared';

const enum Status {
    'AVAILABLE',
    'RESERVED',
    'ONRIDE',
    'COMPLETED'
}

export class RideDetails implements BaseEntity {
    constructor(
        public id?: number,
        public pickedupOn?: any,
        public droppedOn?: any,
        public comment?: string,
        public status?: Status,
        public appUser?: BaseEntity,
        public ride?: BaseEntity,
    ) {
    }
}
