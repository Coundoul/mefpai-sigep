import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEquipement } from '../equipement.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { EquipementService } from '../service/equipement.service';
import { EquipementDeleteDialogComponent } from '../delete/equipement-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';
import { FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'jhi-equipement',
  templateUrl: './equipement.component.html',
  styleUrls: ['./equipement.component.scss'],
})

export class EquipementComponent implements OnInit {
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
  data: any;

  editForm = this.fb.group({
    recherche: [null, [Validators.required]],
  });

  constructor(
    protected equipementService: EquipementService,
    protected activatedRoute: ActivatedRoute,
    protected dataUtils: DataUtils,
    protected router: Router,
    protected modalService: NgbModal,
    protected fb: FormBuilder
  ) {
    this.data = {
      labels: ['A','B','C'],
      datasets: [
          {
              data: [300, 50, 100],
              backgroundColor: [
                  "#FF6384",
                  "#36A2EB",
                  "#FFCE56"
              ],
              hoverBackgroundColor: [
                  "#FF6384",
                  "#36A2EB",
                  "#FFCE56"
              ]
          }]    
      };
  }


  
  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    this.equipementService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IEquipement[]>) => {
          this.isLoading = false;
          this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
        },
        () => {
          this.isLoading = false;
          this.onError();
        }
      );
  }

  ngOnInit(): void {
    this.handleNavigation();
  }

  trackId(index: number, item: IEquipement): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(equipement: IEquipement): void {
    const modalRef = this.modalService.open(EquipementDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.equipement = equipement;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
      }
    });
  }

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

  rechercher(): void {
    this.isSaving = true;
    this.recherche = this.editForm.get(['recherche'])!.value;
    this.router.navigate(['/equipement/search', { reference: this.recherche }]);
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected handleNavigation(): void {
    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
      const page = params.get('page');
      const pageNumber = page !== null ? +page : 1;
      const sort = (params.get('sort') ?? data['defaultSort']).split(',');
      const predicate = sort[0];
      const ascending = sort[1] === 'asc';
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.loadPage(pageNumber, true);
      }
    });
  }

  protected onSuccess(data: IEquipement[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/equipement'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.equipements = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
