import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IResponsable } from '../responsable.model';

@Component({
  selector: 'jhi-responsable-detail',
  templateUrl: './responsable-detail.component.html',
})
export class ResponsableDetailComponent implements OnInit {
  responsable: IResponsable | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ responsable }) => {
      this.responsable = responsable;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
