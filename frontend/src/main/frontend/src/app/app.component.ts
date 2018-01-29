import {Component} from '@angular/core';
import {SearchService} from "./SearchService";
import {NotificationsService, SimpleNotificationsModule} from "angular2-notifications";
import {Result} from "./result";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  queryString = "";
  result = this.emptyResult();
  searchInProgress = false;

  page = 1;
  pageSize = 30;

  constructor(private searchService: SearchService,
              private notifications: NotificationsService) {

  }

  private emptyResult(): Result {
    return {
      hasMore: false,
      items: []
    }
  }

  search(e) {
    if (e) {
      e.preventDefault()
    }

    this.page = 1;
    this.doSearch()
  }

  next() {
    this.page += this.pageSize;

    this.doSearch();
  }

  prev() {
    this.page -= this.pageSize;

    this.doSearch();
  }

  private doSearch() {
    let query = this.queryString.trim();
    if (query == '') {
      // Use validation instead
      return
    }

    this.searchInProgress = true;

    this.searchService
      .search(query, this.page, this.pageSize)
      .subscribe(result => {
        this.result = result;
        this.searchInProgress = false;
      }, err => {
        this.result = this.emptyResult();
        this.searchInProgress = false;
        this.notifications.error(
          "Error",
          "Something went wrong. Please, try again later.",
          {
            timeOut: 3000,
            showProgressBar: true,
            pauseOnHover: false,
            clickToClose: true,
            maxLength: 50
          })
      });
  }

}
