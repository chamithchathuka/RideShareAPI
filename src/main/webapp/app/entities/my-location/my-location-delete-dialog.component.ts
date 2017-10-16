import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { MyLocation } from './my-location.model';
import { MyLocationPopupService } from './my-location-popup.service';
import { MyLocationService } from './my-location.service';

@Component({
    selector: 'jhi-my-location-delete-dialog',
    templateUrl: './my-location-delete-dialog.component.html'
})
export class MyLocationDeleteDialogComponent {

    myLocation: MyLocation;

    constructor(
        private myLocationService: MyLocationService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.myLocationService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'myLocationListModification',
                content: 'Deleted an myLocation'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-my-location-delete-popup',
    template: ''
})
export class MyLocationDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private myLocationPopupService: MyLocationPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.myLocationPopupService
                .open(MyLocationDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
