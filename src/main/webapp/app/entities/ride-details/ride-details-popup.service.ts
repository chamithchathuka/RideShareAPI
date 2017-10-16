import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { RideDetails } from './ride-details.model';
import { RideDetailsService } from './ride-details.service';

@Injectable()
export class RideDetailsPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private rideDetailsService: RideDetailsService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.rideDetailsService.find(id).subscribe((rideDetails) => {
                    rideDetails.pickedupOn = this.datePipe
                        .transform(rideDetails.pickedupOn, 'yyyy-MM-ddThh:mm');
                    rideDetails.droppedOn = this.datePipe
                        .transform(rideDetails.droppedOn, 'yyyy-MM-ddThh:mm');
                    this.ngbModalRef = this.rideDetailsModalRef(component, rideDetails);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.rideDetailsModalRef(component, new RideDetails());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    rideDetailsModalRef(component: Component, rideDetails: RideDetails): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.rideDetails = rideDetails;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
