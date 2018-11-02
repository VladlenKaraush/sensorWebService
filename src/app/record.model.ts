export class SensorRecord {
  temp: number;
  latitude: string;
  longitude: string;

  constructor(temp: number, latitude: string, longitude: string) {
    this.temp = temp;
    this.latitude = latitude;
    this.longitude = longitude;
  }
}
