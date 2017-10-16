import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { MyLocation } from './my-location.model';
import { MyLocationPopupService } from './my-location-popup.service';
import { MyLocationService } from './my-location.service';
import { AppUser, AppUserService } from '../app-user';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-my-location-dialog',
    templateUrl: './my-location-dialog.component.html'
})
export class MyLocationDialogComponent implements OnInit {

    myLocation: MyLocation;
    isSaving: boolean;

    appusers: AppUser[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private myLocationService: MyLocationService,
        private appUserService: AppUserService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.appUserService.query()
            .subscribe((res: ResponseWrapper) => { this.appusers = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.myLocation.id !== undefined) {
            this.subscribeToSaveResponse(
                this.myLocationService.update(this.myLocation));
        } else {
            this.subscribeToSaveResponse(
                this.myLocationService.create(this.myLocation));
        }
    }

    private subscribeToSaveResponse(result: Observable<MyLocation>) {
        result.subscribe((res: MyLocation) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: MyLocation) {
        this.eventManager.broadcast({ name: 'myLocationListModification', content: 'OK'});
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
}

@Component({
    selector: 'jhi-my-location-popup',
    template: ''
})
export class MyLocationPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private myLocationPopupService: MyLocationPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.myLocationPopupService
                    .open(MyLocationDialogComponent as Component, params['id']);
            } else {
                this.myLocationPopupService
                    .open(MyLocationDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
