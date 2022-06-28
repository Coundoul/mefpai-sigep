import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUtilisateurFinal } from '../utilisateur-final.model';

@Component({
  selector: 'jhi-utilisateur-final-detail',
  templateUrl: './utilisateur-final-detail.component.html',
})
export class UtilisateurFinalDetailComponent implements OnInit {
  utilisateurFinal: IUtilisateurFinal | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ utilisateurFinal }) => {
      this.utilisateurFinal = utilisateurFinal;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
