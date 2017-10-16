import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { MyLocation } from './my-location.model';
import { MyLocationService } from './my-location.service';

@Component({
    selector: 'jhi-my-location-detail',
    templateUrl: './my-location-detail.component.html'
})
export class MyLocationDetailComponent implements OnInit, OnDestroy {

    myLocation: MyLocation;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private myLocationService: MyLocationService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInMyLocations();
    }

    load(id) {
        this.myLocationService.find(id).subscribe((myLocation) => {
            this.myLocation = myLocation;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInMyLocations() {
        this.eventSubscriber = this.eventManager.subscribe(
            'myLocationListModification',
            (response) => this.load(this.myLocation.id)
        );
    }
}
