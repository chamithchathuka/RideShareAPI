import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { RideDetails } from './ride-details.model';
import { RideDetailsPopupService } from './ride-details-popup.service';
import { RideDetailsService } from './ride-details.service';

@Component({
    selector: 'jhi-ride-details-delete-dialog',
    templateUrl: './ride-details-delete-dialog.component.html'
})
export class RideDetailsDeleteDialogComponent {

    rideDetails: RideDetails;

    constructor(
        private rideDetailsService: RideDetailsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.rideDetailsService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'rideDetailsListModification',
                content: 'Deleted an rideDetails'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-ride-details-delete-popup',
    template: ''
})
export class RideDetailsDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private rideDetailsPopupService: RideDetailsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.rideDetailsPopupService
                .open(RideDetailsDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
