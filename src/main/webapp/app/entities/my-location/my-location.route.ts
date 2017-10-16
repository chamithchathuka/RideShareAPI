import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { MyLocationComponent } from './my-location.component';
import { MyLocationDetailComponent } from './my-location-detail.component';
import { MyLocationPopupComponent } from './my-location-dialog.component';
import { MyLocationDeletePopupComponent } from './my-location-delete-dialog.component';

export const myLocationRoute: Routes = [
    {
        path: 'my-location',
        component: MyLocationComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'rideShareApp.myLocation.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'my-location/:id',
        component: MyLocationDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'rideShareApp.myLocation.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const myLocationPopupRoute: Routes = [
    {
        path: 'my-location-new',
        component: MyLocationPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'rideShareApp.myLocation.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'my-location/:id/edit',
        component: MyLocationPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'rideShareApp.myLocation.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'my-location/:id/delete',
        component: MyLocationDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'rideShareApp.myLocation.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
