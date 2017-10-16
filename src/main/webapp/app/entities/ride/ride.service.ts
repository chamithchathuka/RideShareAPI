import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';

import { Ride } from './ride.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class RideService {

    private resourceUrl = 'api/rides';
    private resourceSearchUrl = 'api/_search/rides';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(ride: Ride): Observable<Ride> {
        const copy = this.convert(ride);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(ride: Ride): Observable<Ride> {
        const copy = this.convert(ride);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<Ride> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        for (let i = 0; i < jsonResponse.length; i++) {
            this.convertItemFromServer(jsonResponse[i]);
        }
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convertItemFromServer(entity: any) {
        entity.startDateTime = this.dateUtils
            .convertDateTimeFromServer(entity.startDateTime);
        entity.createdDateTime = this.dateUtils
            .convertDateTimeFromServer(entity.createdDateTime);
    }

    private convert(ride: Ride): Ride {
        const copy: Ride = Object.assign({}, ride);

        copy.startDateTime = this.dateUtils.toDate(ride.startDateTime);

        copy.createdDateTime = this.dateUtils.toDate(ride.createdDateTime);
        return copy;
    }
}
