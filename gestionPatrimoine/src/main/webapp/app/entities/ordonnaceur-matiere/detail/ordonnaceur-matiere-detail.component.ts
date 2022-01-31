import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOrdonnaceurMatiere } from '../ordonnaceur-matiere.model';

@Component({
  selector: 'jhi-ordonnaceur-matiere-detail',
  templateUrl: './ordonnaceur-matiere-detail.component.html',
})
export class OrdonnaceurMatiereDetailComponent implements OnInit {
  ordonnaceurMatiere: IOrdonnaceurMatiere | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ordonnaceurMatiere }) => {
      this.ordonnaceurMatiere = ordonnaceurMatiere;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
