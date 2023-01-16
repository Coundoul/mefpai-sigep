import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { BatimentService } from '../service/batiment.service';
import { BatimentDeleteDialogComponent } from '../delete/batiment-delete-dialog.component';
import { IBatiment } from '../batiment.model';


@Component({
  selector: 'jhi-batiment',
  templateUrl: './batiment.component.html'
})
export class BatimentComponent implements OnInit {
  batiments?: IBatiment[];
  etablissement!: any;
  nomEtablissement!: any;
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;


  constructor(
    protected batimentService: BatimentService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  
  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    this.batimentService
      .queryDesignation(this.etablissement, {page: pageToLoad - 1,size: this.itemsPerPage,sort: this.sort(),})
      .subscribe(
        (res: HttpResponse<IBatiment[]>) => {
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
    this.activatedRoute.data.subscribe(() => {
      this.etablissement = Number(this.activatedRoute.snapshot.paramMap.get('id'));
      this.nomEtablissement = this.activatedRoute.snapshot.paramMap.get('nomEtablissement');
    });
    this.handleNavigation();
  }

  trackId(index: number, item: IBatiment): number {
    return item.id!;
  }


  delete(batiment: IBatiment): void {
    const modalRef = this.modalService.open(BatimentDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.batiment = batiment;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
      }
    });
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
  
  protected onSuccess(data: IBatiment[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/etablissement'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.batiments = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
