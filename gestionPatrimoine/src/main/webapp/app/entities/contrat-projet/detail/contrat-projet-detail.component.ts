import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IContratProjet } from '../contrat-projet.model';

@Component({
  selector: 'jhi-contrat-projet-detail',
  templateUrl: './contrat-projet-detail.component.html',
})
export class ContratProjetDetailComponent implements OnInit {
  contratProjet: IContratProjet | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contratProjet }) => {
      this.contratProjet = contratProjet;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
