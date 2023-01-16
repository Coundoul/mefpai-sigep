import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEtapes } from '../etapes.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { EtapesService } from '../service/etapes.service';
import { EtapesDeleteDialogComponent } from '../delete/etapes-delete-dialog.component';

//import { CalendarOptions } from '@fullcalendar/core'; // useful for typechecking
// import dayGridPlugin from '@fullcalendar/daygrid';

@Component({
  selector: 'jhi-etapes',
  templateUrl: './etapes.component.html',
})
export class EtapesComponent implements OnInit {
  etapes?: IEtapes[];
  projet!: any;
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  events: any;

  options: any;

  // calendarOptions: CalendarOptions = {
  //   initialView: 'dayGridMonth',
  //   plugins: [dayGridPlugin]
  // };

  constructor(
    protected etapesService: EtapesService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    this.etapesService
      .queryEtapesProjet(this.projet, {
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IEtapes[]>) => {
          this.isLoading = false;
          this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
        },
        () => {
          this.isLoading = false;
          this.onError();
        }
      );
  }

  /* getCalendrier(): void{
    this.etapesService.getEvent(this.projet).subscribe(events => {this.events = events.body;});

        this.options = {
            plugins: [dayGridPlugin, timeGridPlugin, interactionPlugin],
            defaultDate: '2017-02-01',
            header: {
                left: 'prev,next',
                center: 'title',
                right: 'dayGridMonth,timeGridWeek,timeGridDay'
            }
        }
  } */


  ngOnInit(): void {
    this.activatedRoute.data.subscribe(() => {
      this.projet = Number(this.activatedRoute.snapshot.paramMap.get('id'));
    });

    this.handleNavigation();
    //this.getCalendrier();
  }

  trackId(index: number, item: IEtapes): number {
    return item.id!;
  }

  delete(etapes: IEtapes): void {
    const modalRef = this.modalService.open(EtapesDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.etapes = etapes;
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

  protected onSuccess(data: IEtapes[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/etapes'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.etapes = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
