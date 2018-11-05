import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import { HttpClient } from '@angular/common/http';

import { FormBuilder } from '@angular/forms';
import {SensorRecord} from '../record.model';
import {ServerAccessService} from '../services/serverAccessService';

@Component({
  selector: 'app-record-form',
  templateUrl: './record-form.component.html',
  styleUrls: ['./record-form.component.css']
})
export class RecordFormComponent implements OnInit {

  latitudeDirections = [];
  longitudeDirections = [];
  record;
  constructor(private fb: FormBuilder, private http: HttpClient, private server: ServerAccessService) { }

  ngOnInit() {
    this.latitudeDirections = ['S', 'N'];
    this.longitudeDirections = ['W', 'E'];

    this.record = this.fb.group({
      latitude : this.fb.group({
        degrees: ['', Validators.compose([Validators.required, Validators.min(-90), Validators.max(90)])],
        minutes: ['', Validators.compose([Validators.required, Validators.min(0), Validators.max(60)])],
        seconds: ['', Validators.compose([Validators.required, Validators.min(0), Validators.max(60)])],
        direction: ['N'],
      }),
      longitude : this.fb.group({
        degrees: ['', Validators.compose([Validators.required, Validators.min(-180), Validators.max(180)])],
        minutes: ['', Validators.compose([Validators.required, Validators.min(0), Validators.max(60)])],
        seconds : ['', Validators.compose([Validators.required, Validators.min(0), Validators.max(60)])],
        direction: ['E'],
      }),
      temperature : ['', Validators.compose([Validators.required, Validators.min(-100), Validators.max(100)])]
    });
  }

  onSubmit() {
    console.warn(this.record.value);
    const val = this.record.value;
    const recordDTO: SensorRecord = {
      latitude: val.latitude.degrees + '° ' + val.latitude.minutes + '′ ' + val.latitude.seconds + '″ ' + val.latitude.direction,
      longitude: val.longitude.degrees + '° ' + val.longitude.minutes + '′ ' + val.longitude.seconds + '″ ' + val.longitude.direction,
      temperature: val.temperature,
      city: ''
    };
    console.warn(recordDTO);
    const ans = this.server.storeRecord(recordDTO);
    console.warn(ans);
  }

}
