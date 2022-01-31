import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRequete } from '../requete.model';

@Component({
  selector: 'jhi-requete-detail',
  templateUrl: './requete-detail.component.html',
})
export class RequeteDetailComponent implements OnInit {
  requete: IRequete | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ requete }) => {
      this.requete = requete;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
