import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {SensorRecord} from '../record.model';
import {ServerAccessService} from '../services/serverAccessService';

@Component({
  selector: 'app-sensor-list',
  templateUrl: './sensor-list.component.html',
  styleUrls: ['./sensor-list.component.css']
})
export class SensorListComponent implements OnInit {
  list = [];
  recordsUrl = 'http://localhost:8080/records';
  constructor(private http: HttpClient, private server: ServerAccessService) {
  }

  ngOnInit() {
     this.fetchRecords();
     this.server.recordsUpdated.subscribe(
       (records: SensorRecord[]) => {
         this.list = records;
       }
     );
  }

  fetchRecords () {
    this.http.get<SensorRecord[]>(this.recordsUrl)
      .subscribe(
        (records: SensorRecord[]) => {
          console.log(records);
          this.list = records;
        }
      );
  }

  private handleError<T> (operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }
}
