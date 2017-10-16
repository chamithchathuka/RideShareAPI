import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RideShareSharedModule } from '../../shared';
import {
    RideDetailsService,
    RideDetailsPopupService,
    RideDetailsComponent,
    RideDetailsDetailComponent,
    RideDetailsDialogComponent,
    RideDetailsPopupComponent,
    RideDetailsDeletePopupComponent,
    RideDetailsDeleteDialogComponent,
    rideDetailsRoute,
    rideDetailsPopupRoute,
} from './';

const ENTITY_STATES = [
    ...rideDetailsRoute,
    ...rideDetailsPopupRoute,
];

@NgModule({
    imports: [
        RideShareSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        RideDetailsComponent,
        RideDetailsDetailComponent,
        RideDetailsDialogComponent,
        RideDetailsDeleteDialogComponent,
        RideDetailsPopupComponent,
        RideDetailsDeletePopupComponent,
    ],
    entryComponents: [
        RideDetailsComponent,
        RideDetailsDialogComponent,
        RideDetailsPopupComponent,
        RideDetailsDeleteDialogComponent,
        RideDetailsDeletePopupComponent,
    ],
    providers: [
        RideDetailsService,
        RideDetailsPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RideShareRideDetailsModule {}
