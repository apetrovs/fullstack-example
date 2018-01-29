import {BrowserModule} from '@angular/platform-browser';
import {HttpClientModule} from '@angular/common/http';
import {NgModule} from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations'


import {AppComponent} from './app.component';
import {SimpleNotificationsModule} from "angular2-notifications";
import {SearchService} from "./SearchService";


@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    SimpleNotificationsModule.forRoot()
  ],
  providers: [SearchService],
  bootstrap: [AppComponent]
})
export class AppModule {
}
