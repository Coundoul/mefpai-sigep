import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IChefEtablissement } from '../chef-etablissement.model';

@Component({
  selector: 'jhi-chef-etablissement-detail',
  templateUrl: './chef-etablissement-detail.component.html',
})
export class ChefEtablissementDetailComponent implements OnInit {
  chefEtablissement: IChefEtablissement | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ chefEtablissement }) => {
      this.chefEtablissement = chefEtablissement;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
