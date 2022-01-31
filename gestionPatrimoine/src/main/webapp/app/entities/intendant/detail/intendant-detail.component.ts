import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIntendant } from '../intendant.model';

@Component({
  selector: 'jhi-intendant-detail',
  templateUrl: './intendant-detail.component.html',
})
export class IntendantDetailComponent implements OnInit {
  intendant: IIntendant | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ intendant }) => {
      this.intendant = intendant;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
