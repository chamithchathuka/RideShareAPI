import { BaseEntity } from './../../shared';

const enum RideType {
    'REQUEST',
    'SHARE'
}

const enum Privacy {
    'FRIENDONLY',
    'PUBLIC'
}

export class Ride implements BaseEntity {
    constructor(
        public id?: number,
        public startLocation?: string,
        public endLocation?: string,
        public startLocaionLat?: string,
        public startLocationLong?: string,
        public endLocaionLat?: string,
        public endLocationLong?: string,
        public startDateTime?: any,
        public createdDateTime?: any,
        public seatCapasity?: number,
        public rideType?: RideType,
        public privacy?: Privacy,
        public rideDetails?: BaseEntity[],
        public appUser?: BaseEntity,
    ) {
    }
}
