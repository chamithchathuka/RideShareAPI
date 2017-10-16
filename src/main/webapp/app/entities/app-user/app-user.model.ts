import { BaseEntity } from './../../shared';

const enum Gender {
    'MALE',
    'FEMALE'
}

export class AppUser implements BaseEntity {
    constructor(
        public id?: number,
        public iuserId?: number,
        public firstName?: string,
        public lastName?: string,
        public gender?: Gender,
        public address?: string,
        public email?: string,
        public fbID?: string,
        public googleID?: string,
        public linkedInID?: string,
        public rides?: BaseEntity[],
        public rideDetails?: BaseEntity[],
        public myLocations?: BaseEntity[],
    ) {
    }
}
