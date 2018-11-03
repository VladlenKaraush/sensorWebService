import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup} from '@angular/forms';

@Component({
  selector: 'app-record-form',
  templateUrl: './record-form.component.html',
  styleUrls: ['./record-form.component.css']
})
export class RecordFormComponent implements OnInit {

  latitudeDirections = [];
  longitudeDirections = [];
  record;
  constructor() { }

  ngOnInit() {
    this.latitudeDirections = ['S', 'N'];
    this.longitudeDirections = ['W', 'E'];

    this.record = new FormGroup({
      latitude : new FormGroup({
        degrees: new FormControl(''),
        minutes: new FormControl(''),
        seconds: new FormControl(''),
        direction: new FormControl(''),
      }),
      longitude : new FormGroup({
        degrees: new FormControl(''),
        minutes: new FormControl(''),
        seconds : new FormControl(''),
        direction: new FormControl(''),
      }),
      temperature : new FormControl('')
    });
  }

  onSubmit() {
    // TODO: Use EventEmitter with form value
    console.warn(this.record.value);
  }

}
