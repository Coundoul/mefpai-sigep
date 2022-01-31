import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IChefProjet } from '../chef-projet.model';

@Component({
  selector: 'jhi-chef-projet-detail',
  templateUrl: './chef-projet-detail.component.html',
})
export class ChefProjetDetailComponent implements OnInit {
  chefProjet: IChefProjet | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ chefProjet }) => {
      this.chefProjet = chefProjet;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
