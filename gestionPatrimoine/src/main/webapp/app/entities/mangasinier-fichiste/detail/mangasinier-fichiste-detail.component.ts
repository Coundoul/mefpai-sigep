import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMangasinierFichiste } from '../mangasinier-fichiste.model';

@Component({
  selector: 'jhi-mangasinier-fichiste-detail',
  templateUrl: './mangasinier-fichiste-detail.component.html',
})
export class MangasinierFichisteDetailComponent implements OnInit {
  mangasinierFichiste: IMangasinierFichiste | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mangasinierFichiste }) => {
      this.mangasinierFichiste = mangasinierFichiste;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
