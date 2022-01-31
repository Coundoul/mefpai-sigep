import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICategorieMatiere } from '../categorie-matiere.model';

@Component({
  selector: 'jhi-categorie-matiere-detail',
  templateUrl: './categorie-matiere-detail.component.html',
})
export class CategorieMatiereDetailComponent implements OnInit {
  categorieMatiere: ICategorieMatiere | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ categorieMatiere }) => {
      this.categorieMatiere = categorieMatiere;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
