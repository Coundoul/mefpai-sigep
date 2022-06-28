import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITechnicien } from '../technicien.model';

@Component({
  selector: 'jhi-technicien-detail',
  templateUrl: './technicien-detail.component.html',
})
export class TechnicienDetailComponent implements OnInit {
  technicien: ITechnicien | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ technicien }) => {
      this.technicien = technicien;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
