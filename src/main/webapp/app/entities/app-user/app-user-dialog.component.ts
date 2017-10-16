import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { AppUser } from './app-user.model';
import { AppUserPopupService } from './app-user-popup.service';
import { AppUserService } from './app-user.service';

@Component({
    selector: 'jhi-app-user-dialog',
    templateUrl: './app-user-dialog.component.html'
})
export class AppUserDialogComponent implements OnInit {

    appUser: AppUser;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private appUserService: AppUserService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.appUser.id !== undefined) {
            this.subscribeToSaveResponse(
                this.appUserService.update(this.appUser));
        } else {
            this.subscribeToSaveResponse(
                this.appUserService.create(this.appUser));
        }
    }

    private subscribeToSaveResponse(result: Observable<AppUser>) {
        result.subscribe((res: AppUser) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: AppUser) {
        this.eventManager.broadcast({ name: 'appUserListModification', content: 'OK'});
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
}

@Component({
    selector: 'jhi-app-user-popup',
    template: ''
})
export class AppUserPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private appUserPopupService: AppUserPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.appUserPopupService
                    .open(AppUserDialogComponent as Component, params['id']);
            } else {
                this.appUserPopupService
                    .open(AppUserDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
