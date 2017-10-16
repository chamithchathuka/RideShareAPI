import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RideShareSharedModule } from '../../shared';
import {
    MyLocationService,
    MyLocationPopupService,
    MyLocationComponent,
    MyLocationDetailComponent,
    MyLocationDialogComponent,
    MyLocationPopupComponent,
    MyLocationDeletePopupComponent,
    MyLocationDeleteDialogComponent,
    myLocationRoute,
    myLocationPopupRoute,
} from './';

const ENTITY_STATES = [
    ...myLocationRoute,
    ...myLocationPopupRoute,
];

@NgModule({
    imports: [
        RideShareSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        MyLocationComponent,
        MyLocationDetailComponent,
        MyLocationDialogComponent,
        MyLocationDeleteDialogComponent,
        MyLocationPopupComponent,
        MyLocationDeletePopupComponent,
    ],
    entryComponents: [
        MyLocationComponent,
        MyLocationDialogComponent,
        MyLocationPopupComponent,
        MyLocationDeleteDialogComponent,
        MyLocationDeletePopupComponent,
    ],
    providers: [
        MyLocationService,
        MyLocationPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RideShareMyLocationModule {}
