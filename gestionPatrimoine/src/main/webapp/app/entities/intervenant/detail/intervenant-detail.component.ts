import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIntervenant } from '../intervenant.model';

@Component({
  selector: 'jhi-intervenant-detail',
  templateUrl: './intervenant-detail.component.html',
})
export class IntervenantDetailComponent implements OnInit {
  intervenant: IIntervenant | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ intervenant }) => {
      this.intervenant = intervenant;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
