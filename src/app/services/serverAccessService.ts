import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {SensorRecord} from '../record.model';
import {Subject} from 'rxjs';


@Injectable({
  providedIn: 'root',
})
export class ServerAccessService {

  constructor(private http: HttpClient) {
  }
  private URL = 'http://localhost:8080/records';
  private API_URL = 'https://maps.googleapis.com/maps/api/geocode/json';
  private API_KEY = 'AIzaSyCDDLrbcbOzcuGrU0h6gUN8bPubuCHpooU';

  private records: SensorRecord[];
  recordsUpdated = new Subject<SensorRecord[]>();

  async updateRecords() {
    await this.sleep(100);
    const that = this;
    console.warn('updating');
    this.http.get<SensorRecord[]>(this.URL)
      .subscribe(
        (records: SensorRecord[]) => {
          this.records = records;
          this.recordsUpdated.next(this.records);
          this.reverseGeocode();
          console.warn('done geocoding');
          console.warn('updated records');
        }
      );
  }

  sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
  }

  storeRecord(record: SensorRecord) {
    const ans = this.http.post(this.URL, record);
    ans.subscribe(obj => console.warn(obj));
    this.updateRecords();
  }

  reverseGeocode() {
    const result = this.records;
    let counter = this.records.length;
    for (let i = 0; i < this.records.length; i++) {
      const lat = this.parseLocation(this.records[i].latitude);
      const lng = this.parseLocation(this.records[i].longitude);
      const params = new HttpParams()
      // .set(' address', 'Walt Disney World Resort, Orlando, FL 32830, USA')
        .set('latlng', lat.toString() + ', ' + lng.toString())
        .set('key', this.API_KEY);
      const ans = this.http.get(this.API_URL, {params: params});
      let str;
      ans.subscribe((obj: any) => {
        console.warn('in observable, i = ' + i);
        console.log(obj);
        if (obj.status === 'OK') {
          str = obj.results[obj.results.length - 2].formatted_address;
        } else {
          str = ' - ';
        }
          result[i].city = str;
        counter --;
        if (counter === 0) {
          this.recordsUpdated.next(result);
        }
      });
    }
  }

  parseLocation(string) {
    let rest = string;
    let result = 0;
    if (rest.includes('°')) {
      const deg = parseFloat(rest.split('°')[0]);
      rest = string.split('°')[1];
      result += deg;
    }

    if (rest.includes('′')) {
      const min = parseFloat(rest.split('′')[0]);
      rest = rest.split('′')[1];
      result += min / 60;
    }

    if (rest.includes('″')) {
      const sec = parseFloat(rest.split('″')[0]);
      result += sec / 3600;
    }

    return result;
  }
}
