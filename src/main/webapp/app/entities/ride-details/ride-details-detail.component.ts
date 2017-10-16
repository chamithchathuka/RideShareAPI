import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { RideDetails } from './ride-details.model';
import { RideDetailsService } from './ride-details.service';

@Component({
    selector: 'jhi-ride-details-detail',
    templateUrl: './ride-details-detail.component.html'
})
export class RideDetailsDetailComponent implements OnInit, OnDestroy {

    rideDetails: RideDetails;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private rideDetailsService: RideDetailsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInRideDetails();
    }

    load(id) {
        this.rideDetailsService.find(id).subscribe((rideDetails) => {
            this.rideDetails = rideDetails;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInRideDetails() {
        this.eventSubscriber = this.eventManager.subscribe(
            'rideDetailsListModification',
            (response) => this.load(this.rideDetails.id)
        );
    }
}
