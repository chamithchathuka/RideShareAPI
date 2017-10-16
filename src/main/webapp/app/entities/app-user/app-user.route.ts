import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { AppUserComponent } from './app-user.component';
import { AppUserDetailComponent } from './app-user-detail.component';
import { AppUserPopupComponent } from './app-user-dialog.component';
import { AppUserDeletePopupComponent } from './app-user-delete-dialog.component';

export const appUserRoute: Routes = [
    {
        path: 'app-user',
        component: AppUserComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'rideShareApp.appUser.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'app-user/:id',
        component: AppUserDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'rideShareApp.appUser.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const appUserPopupRoute: Routes = [
    {
        path: 'app-user-new',
        component: AppUserPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'rideShareApp.appUser.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'app-user/:id/edit',
        component: AppUserPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'rideShareApp.appUser.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'app-user/:id/delete',
        component: AppUserDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'rideShareApp.appUser.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
