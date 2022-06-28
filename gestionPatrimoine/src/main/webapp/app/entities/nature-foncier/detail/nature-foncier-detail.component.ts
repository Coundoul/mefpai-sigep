import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { INatureFoncier } from '../nature-foncier.model';

@Component({
  selector: 'jhi-nature-foncier-detail',
  templateUrl: './nature-foncier-detail.component.html',
})
export class NatureFoncierDetailComponent implements OnInit {
  natureFoncier: INatureFoncier | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ natureFoncier }) => {
      this.natureFoncier = natureFoncier;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
