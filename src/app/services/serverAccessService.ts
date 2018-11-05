import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {SensorRecord} from '../record.model';
import {Subject} from 'rxjs';


@Injectable({
  providedIn: 'root',
})
export class ServerAccessService {

  constructor(private http: HttpClient) {
  }
  private URL = 'http://localhost:8080/records';
  private records: SensorRecord[];
  recordsUpdated = new Subject<SensorRecord[]>();

  updateRecords() {
    this.http.get<SensorRecord[]>(this.URL)
      .subscribe(
        (records: SensorRecord[]) => {
          this.records = records;
          this.recordsUpdated.next(this.records);
          console.warn('updated records');
        }
      );
  }

  storeRecord(record: SensorRecord) {
    const ans = this.http.post(this.URL, record);
    ans.subscribe(obj => console.warn(obj));
    this.updateRecords();
  }

  getRecords(): SensorRecord[] {
    return this.records;
  }
}
