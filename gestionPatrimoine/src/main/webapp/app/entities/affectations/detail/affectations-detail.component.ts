import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DataUtils } from 'app/core/util/data-util.service';
import { EquipementService } from 'app/entities/equipement/service/equipement.service';

import { IAffectations } from '../affectations.model';

@Component({
  selector: 'jhi-affectations-detail',
  templateUrl: './affectations-detail.component.html',
})
export class AffectationsDetailComponent implements OnInit {
  affectations: IAffectations | null = null;
  equipement: any;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ affectations }) => {
      this.affectations = affectations;
      //this.equipement = equipement;
    });
  }

  byteSize(base64String: any): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: any, contentType: any | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
