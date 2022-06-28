import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProjetAttribution } from '../projet-attribution.model';

@Component({
  selector: 'jhi-projet-attribution-detail',
  templateUrl: './projet-attribution-detail.component.html',
})
export class ProjetAttributionDetailComponent implements OnInit {
  projetAttribution: IProjetAttribution | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ projetAttribution }) => {
      this.projetAttribution = projetAttribution;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
