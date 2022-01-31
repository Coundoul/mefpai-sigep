import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAttributionInfrastructure } from '../attribution-infrastructure.model';

@Component({
  selector: 'jhi-attribution-infrastructure-detail',
  templateUrl: './attribution-infrastructure-detail.component.html',
})
export class AttributionInfrastructureDetailComponent implements OnInit {
  attributionInfrastructure: IAttributionInfrastructure | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ attributionInfrastructure }) => {
      this.attributionInfrastructure = attributionInfrastructure;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
