import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFiliereStabilise } from '../filiere-stabilise.model';

@Component({
  selector: 'jhi-filiere-stabilise-detail',
  templateUrl: './filiere-stabilise-detail.component.html',
})
export class FiliereStabiliseDetailComponent implements OnInit {
  filiereStabilise: IFiliereStabilise | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ filiereStabilise }) => {
      this.filiereStabilise = filiereStabilise;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
