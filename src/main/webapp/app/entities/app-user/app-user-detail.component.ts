import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { AppUser } from './app-user.model';
import { AppUserService } from './app-user.service';

@Component({
    selector: 'jhi-app-user-detail',
    templateUrl: './app-user-detail.component.html'
})
export class AppUserDetailComponent implements OnInit, OnDestroy {

    appUser: AppUser;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private appUserService: AppUserService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInAppUsers();
    }

    load(id) {
        this.appUserService.find(id).subscribe((appUser) => {
            this.appUser = appUser;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInAppUsers() {
        this.eventSubscriber = this.eventManager.subscribe(
            'appUserListModification',
            (response) => this.load(this.appUser.id)
        );
    }
}
