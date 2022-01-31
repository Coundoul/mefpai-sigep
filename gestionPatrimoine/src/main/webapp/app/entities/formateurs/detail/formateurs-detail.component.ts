import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFormateurs } from '../formateurs.model';

@Component({
  selector: 'jhi-formateurs-detail',
  templateUrl: './formateurs-detail.component.html',
})
export class FormateursDetailComponent implements OnInit {
  formateurs: IFormateurs | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ formateurs }) => {
      this.formateurs = formateurs;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
