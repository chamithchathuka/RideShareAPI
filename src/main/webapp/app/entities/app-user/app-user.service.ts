import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { AppUser } from './app-user.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class AppUserService {

    private resourceUrl = 'api/app-users';
    private resourceSearchUrl = 'api/_search/app-users';

    constructor(private http: Http) { }

    create(appUser: AppUser): Observable<AppUser> {
        const copy = this.convert(appUser);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(appUser: AppUser): Observable<AppUser> {
        const copy = this.convert(appUser);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<AppUser> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
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
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convert(appUser: AppUser): AppUser {
        const copy: AppUser = Object.assign({}, appUser);
        return copy;
    }
}
