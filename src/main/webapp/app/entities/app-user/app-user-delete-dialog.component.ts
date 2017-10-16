import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { AppUser } from './app-user.model';
import { AppUserPopupService } from './app-user-popup.service';
import { AppUserService } from './app-user.service';

@Component({
    selector: 'jhi-app-user-delete-dialog',
    templateUrl: './app-user-delete-dialog.component.html'
})
export class AppUserDeleteDialogComponent {

    appUser: AppUser;

    constructor(
        private appUserService: AppUserService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.appUserService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'appUserListModification',
                content: 'Deleted an appUser'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-app-user-delete-popup',
    template: ''
})
export class AppUserDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private appUserPopupService: AppUserPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.appUserPopupService
                .open(AppUserDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
