export class SensorRecord {
  temperature: number;
  latitude: string;
  longitude: string;
  city: string;

  constructor(temperature: number, latitude: string, longitude: string) {
    this.temperature = temperature;
    this.latitude = latitude;
    this.longitude = longitude;
    this.city = '';
  }
}
