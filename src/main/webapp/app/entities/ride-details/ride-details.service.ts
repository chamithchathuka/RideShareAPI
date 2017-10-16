import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';

import { RideDetails } from './ride-details.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class RideDetailsService {

    private resourceUrl = 'api/ride-details';
    private resourceSearchUrl = 'api/_search/ride-details';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(rideDetails: RideDetails): Observable<RideDetails> {
        const copy = this.convert(rideDetails);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(rideDetails: RideDetails): Observable<RideDetails> {
        const copy = this.convert(rideDetails);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<RideDetails> {
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
        entity.pickedupOn = this.dateUtils
            .convertDateTimeFromServer(entity.pickedupOn);
        entity.droppedOn = this.dateUtils
            .convertDateTimeFromServer(entity.droppedOn);
    }

    private convert(rideDetails: RideDetails): RideDetails {
        const copy: RideDetails = Object.assign({}, rideDetails);

        copy.pickedupOn = this.dateUtils.toDate(rideDetails.pickedupOn);

        copy.droppedOn = this.dateUtils.toDate(rideDetails.droppedOn);
        return copy;
    }
}
