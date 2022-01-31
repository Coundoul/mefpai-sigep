import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBureau } from '../bureau.model';

@Component({
  selector: 'jhi-bureau-detail',
  templateUrl: './bureau-detail.component.html',
})
export class BureauDetailComponent implements OnInit {
  bureau: IBureau | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bureau }) => {
      this.bureau = bureau;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
