import { HttpResponse } from '@angular/common/http';
import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { DataUtils } from 'app/core/util/data-util.service';
import { IEquipement } from '../equipement.model';
import { EquipementService } from '../service/equipement.service';

@Component({
  selector: 'jhi-recherche-equipement',
  templateUrl: './recherche-equipement.component.html',
  styleUrls: ['./recherche-equipement.component.scss'],
})
export class RechercheEquipementComponent implements OnInit {
  isSaving = false;
  equipements?: IEquipement[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  recherche: any;
  ascending!: boolean;
  ngbPaginationPage = 1;
  detailRecherche: any;

  editForm = this.fb.group({
    recherche: [null, [Validators.required]],
  });
  sub: any;

  constructor(
    protected equipementService: EquipementService,
    protected activatedRoute: ActivatedRoute,
    protected dataUtils: DataUtils,
    protected router: Router,
    protected modalService: NgbModal,
    protected fb: FormBuilder
  ) {}

  getStatDetail(ref: any): void {
    this.equipementService.rechercher(ref).subscribe(
      (res: HttpResponse<any>) => {
        this.isLoading = false;
        this.detailRecherche = res.body;
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {

    this.sub = this.activatedRoute.params.subscribe(params => {
      this.recherche = this.activatedRoute.snapshot.paramMap.get('reference');
      if (this.recherche !== undefined){
        this.getStatDetail(this.recherche);
      }
    });
  }

  rechercher(): void {
    this.isSaving = true;
    this.recherche = this.editForm.get(['recherche'])!.value;
    this.router.navigate(['/equipement/search', { reference: this.recherche }]);
    this.ngOnInit();
  }

  previousState(): void {
    this.router.navigate(['/equipement']);
  }
}
