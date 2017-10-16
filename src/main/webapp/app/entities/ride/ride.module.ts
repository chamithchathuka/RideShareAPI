import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RideShareSharedModule } from '../../shared';
import {
    RideService,
    RidePopupService,
    RideComponent,
    RideDetailComponent,
    RideDialogComponent,
    RidePopupComponent,
    RideDeletePopupComponent,
    RideDeleteDialogComponent,
    rideRoute,
    ridePopupRoute,
} from './';

const ENTITY_STATES = [
    ...rideRoute,
    ...ridePopupRoute,
];

@NgModule({
    imports: [
        RideShareSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        RideComponent,
        RideDetailComponent,
        RideDialogComponent,
        RideDeleteDialogComponent,
        RidePopupComponent,
        RideDeletePopupComponent,
    ],
    entryComponents: [
        RideComponent,
        RideDialogComponent,
        RidePopupComponent,
        RideDeleteDialogComponent,
        RideDeletePopupComponent,
    ],
    providers: [
        RideService,
        RidePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RideShareRideModule {}
