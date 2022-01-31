import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITypeBatiment } from '../type-batiment.model';

@Component({
  selector: 'jhi-type-batiment-detail',
  templateUrl: './type-batiment-detail.component.html',
})
export class TypeBatimentDetailComponent implements OnInit {
  typeBatiment: ITypeBatiment | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typeBatiment }) => {
      this.typeBatiment = typeBatiment;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
