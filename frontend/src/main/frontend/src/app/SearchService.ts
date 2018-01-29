import {Injectable} from "@angular/core";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {Result} from "./result";
import {Observable} from 'rxjs/Observable'
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/map';
import 'rxjs/add/observable/throw';

@Injectable()
export class SearchService {

  constructor(private _http: HttpClient) {
  }

  search(query: string, page: number, pageSize: number): Observable<Result> {
    return this._http
      .get<Result>(`./search?query=${query}&page=${page}&pageSize=${pageSize}`)
      .catch(this.handleError);
  }

  private handleError(err: HttpErrorResponse) {
    console.error(err);
    let errorMessage = '';
    if (err.error instanceof Error) {
      errorMessage = `An error occurred: ${err.error.message}`;
    } else {
      errorMessage = `Server returned code: ${err.status}, error message is: ${err.message}`;
    }
    console.error(errorMessage);
    return Observable.throw(errorMessage);
  }
}
