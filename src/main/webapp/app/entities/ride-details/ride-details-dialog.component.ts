import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { RideDetails } from './ride-details.model';
import { RideDetailsPopupService } from './ride-details-popup.service';
import { RideDetailsService } from './ride-details.service';
import { AppUser, AppUserService } from '../app-user';
import { Ride, RideService } from '../ride';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-ride-details-dialog',
    templateUrl: './ride-details-dialog.component.html'
})
export class RideDetailsDialogComponent implements OnInit {

    rideDetails: RideDetails;
    isSaving: boolean;

    appusers: AppUser[];

    rides: Ride[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private rideDetailsService: RideDetailsService,
        private appUserService: AppUserService,
        private rideService: RideService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.appUserService.query()
            .subscribe((res: ResponseWrapper) => { this.appusers = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.rideService.query()
            .subscribe((res: ResponseWrapper) => { this.rides = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.rideDetails.id !== undefined) {
            this.subscribeToSaveResponse(
                this.rideDetailsService.update(this.rideDetails));
        } else {
            this.subscribeToSaveResponse(
                this.rideDetailsService.create(this.rideDetails));
        }
    }

    private subscribeToSaveResponse(result: Observable<RideDetails>) {
        result.subscribe((res: RideDetails) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: RideDetails) {
        this.eventManager.broadcast({ name: 'rideDetailsListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    trackAppUserById(index: number, item: AppUser) {
        return item.id;
    }

    trackRideById(index: number, item: Ride) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-ride-details-popup',
    template: ''
})
export class RideDetailsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private rideDetailsPopupService: RideDetailsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.rideDetailsPopupService
                    .open(RideDetailsDialogComponent as Component, params['id']);
            } else {
                this.rideDetailsPopupService
                    .open(RideDetailsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
