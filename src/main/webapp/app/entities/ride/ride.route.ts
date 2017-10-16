import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { RideComponent } from './ride.component';
import { RideDetailComponent } from './ride-detail.component';
import { RidePopupComponent } from './ride-dialog.component';
import { RideDeletePopupComponent } from './ride-delete-dialog.component';

export const rideRoute: Routes = [
    {
        path: 'ride',
        component: RideComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'rideShareApp.ride.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'ride/:id',
        component: RideDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'rideShareApp.ride.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const ridePopupRoute: Routes = [
    {
        path: 'ride-new',
        component: RidePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'rideShareApp.ride.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'ride/:id/edit',
        component: RidePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'rideShareApp.ride.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'ride/:id/delete',
        component: RideDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'rideShareApp.ride.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
