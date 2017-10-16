import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { RideDetailsComponent } from './ride-details.component';
import { RideDetailsDetailComponent } from './ride-details-detail.component';
import { RideDetailsPopupComponent } from './ride-details-dialog.component';
import { RideDetailsDeletePopupComponent } from './ride-details-delete-dialog.component';

export const rideDetailsRoute: Routes = [
    {
        path: 'ride-details',
        component: RideDetailsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'rideShareApp.rideDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'ride-details/:id',
        component: RideDetailsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'rideShareApp.rideDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const rideDetailsPopupRoute: Routes = [
    {
        path: 'ride-details-new',
        component: RideDetailsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'rideShareApp.rideDetails.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'ride-details/:id/edit',
        component: RideDetailsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'rideShareApp.rideDetails.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'ride-details/:id/delete',
        component: RideDetailsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'rideShareApp.rideDetails.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
