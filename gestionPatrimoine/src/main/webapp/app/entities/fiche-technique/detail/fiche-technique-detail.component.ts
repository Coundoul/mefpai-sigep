import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFicheTechnique } from '../fiche-technique.model';

@Component({
  selector: 'jhi-fiche-technique-detail',
  templateUrl: './fiche-technique-detail.component.html',
})
export class FicheTechniqueDetailComponent implements OnInit {
  ficheTechnique: IFicheTechnique | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ficheTechnique }) => {
      this.ficheTechnique = ficheTechnique;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
