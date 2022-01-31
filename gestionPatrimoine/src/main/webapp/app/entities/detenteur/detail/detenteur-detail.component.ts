import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDetenteur } from '../detenteur.model';

@Component({
  selector: 'jhi-detenteur-detail',
  templateUrl: './detenteur-detail.component.html',
})
export class DetenteurDetailComponent implements OnInit {
  detenteur: IDetenteur | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ detenteur }) => {
      this.detenteur = detenteur;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
