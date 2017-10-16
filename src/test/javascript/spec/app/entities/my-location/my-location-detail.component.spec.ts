/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { RideShareTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { MyLocationDetailComponent } from '../../../../../../main/webapp/app/entities/my-location/my-location-detail.component';
import { MyLocationService } from '../../../../../../main/webapp/app/entities/my-location/my-location.service';
import { MyLocation } from '../../../../../../main/webapp/app/entities/my-location/my-location.model';

describe('Component Tests', () => {

    describe('MyLocation Management Detail Component', () => {
        let comp: MyLocationDetailComponent;
        let fixture: ComponentFixture<MyLocationDetailComponent>;
        let service: MyLocationService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [RideShareTestModule],
                declarations: [MyLocationDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    MyLocationService,
                    JhiEventManager
                ]
            }).overrideTemplate(MyLocationDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MyLocationDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MyLocationService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new MyLocation(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.myLocation).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
