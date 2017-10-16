import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';

import { RideDetails } from './ride-details.model';
import { RideDetailsService } from './ride-details.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-ride-details',
    templateUrl: './ride-details.component.html'
})
export class RideDetailsComponent implements OnInit, OnDestroy {
rideDetails: RideDetails[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private rideDetailsService: RideDetailsService,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.rideDetailsService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: ResponseWrapper) => this.rideDetails = res.json,
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
       }
        this.rideDetailsService.query().subscribe(
            (res: ResponseWrapper) => {
                this.rideDetails = res.json;
                this.currentSearch = '';
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInRideDetails();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: RideDetails) {
        return item.id;
    }
    registerChangeInRideDetails() {
        this.eventSubscriber = this.eventManager.subscribe('rideDetailsListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
