import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { RideShareAppUserModule } from './app-user/app-user.module';
import { RideShareMyLocationModule } from './my-location/my-location.module';
import { RideShareRideModule } from './ride/ride.module';
import { RideShareRideDetailsModule } from './ride-details/ride-details.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        RideShareAppUserModule,
        RideShareMyLocationModule,
        RideShareRideModule,
        RideShareRideDetailsModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RideShareEntityModule {}
