import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEquipement } from '../equipement.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-equipement-detail',
  templateUrl: './equipement-detail.component.html',
})
export class EquipementDetailComponent implements OnInit {
  equipement: IEquipement | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ equipement }) => {
      this.equipement = equipement;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
