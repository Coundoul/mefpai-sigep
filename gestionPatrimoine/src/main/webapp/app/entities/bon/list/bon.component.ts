import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBon } from '../bon.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { BonService } from '../service/bon.service';
import { BonDeleteDialogComponent } from '../delete/bon-delete-dialog.component';
import { BonUpdateComponent } from '../update/bon-update.component';

@Component({
  selector: 'jhi-bon',
  templateUrl: './bon.component.html',
})
export class BonComponent implements OnInit {
  bons?: IBon[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  constructor(
    protected bonService: BonService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal,
    public activeModal: NgbActiveModal
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    this.bonService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IBon[]>) => {
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

  trackId(index: number, item: IBon): number {
    return item.id!;
  }

  create(): void {
    const modalRef = this.modalService.open(BonUpdateComponent, { size: 'lg', backdrop: 'static' });
    //modalRef.componentInstance.bon = bon;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'create') {
        this.loadPage();
      }
    });
  }

  cancel(): void {
    this.activeModal.dismiss();
  }

  delete(bon: IBon): void {
    const modalRef = this.modalService.open(BonDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.bon = bon;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
      }
    });
  }

  // update(bon: IBon): void {
  //   const modalRef = this.modalService.open(BonUpdateComponent, { size: 'lg', backdrop: 'static' });
  //   modalRef.componentInstance.bon = bon;
  //   // unsubscribe not needed because closed completes on modal close
  //   modalRef.closed.subscribe(reason => {
  //     if (reason === 'updated') {
  //       this.loadPage();
  //     }
  //   });
  // }

  // addBon(): void {
  //   const modalRef = this.modalService.open(BonUpdateComponent, { size: 'lg', backdrop: 'static' });
  //   modalRef.componentInstance.bon = null;
  //   // unsubscribe not needed because closed completes on modal close
  //   modalRef.closed.subscribe(reason => {
  //     if(reason === 'saved'){
  //       this.loadPage();
  //     }
  //   });
  // }

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

  protected onSuccess(data: IBon[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/bon'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.bons = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
