/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { RideShareTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { RideDetailsDetailComponent } from '../../../../../../main/webapp/app/entities/ride-details/ride-details-detail.component';
import { RideDetailsService } from '../../../../../../main/webapp/app/entities/ride-details/ride-details.service';
import { RideDetails } from '../../../../../../main/webapp/app/entities/ride-details/ride-details.model';

describe('Component Tests', () => {

    describe('RideDetails Management Detail Component', () => {
        let comp: RideDetailsDetailComponent;
        let fixture: ComponentFixture<RideDetailsDetailComponent>;
        let service: RideDetailsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [RideShareTestModule],
                declarations: [RideDetailsDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    RideDetailsService,
                    JhiEventManager
                ]
            }).overrideTemplate(RideDetailsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(RideDetailsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RideDetailsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new RideDetails(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.rideDetails).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
