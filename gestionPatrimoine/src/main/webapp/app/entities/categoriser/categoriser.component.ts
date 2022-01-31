import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { DataUtils } from 'app/core/util/data-util.service';
import { IEtatCategorie } from './categoriser.model';
import { CategoriserService } from './categoriser.service';

@Component({
  selector: 'jhi-categoriser',
  templateUrl: './categoriser.component.html',
  styleUrls: ['./categoriser.component.scss']
})
export class CategoriserComponent implements OnInit {

  categoriserMatieres:any;
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  valeurInventaire: any = 1;

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected dataUtils: DataUtils,
    protected router: Router,
    protected modalService: NgbModal,
    protected categoriematiereService : CategoriserService,
    public dialog: MatDialog,
  ) { }

  getStat():void{
    this.categoriematiereService
      .listEtatCategorieMatiere()
      .subscribe(
        (res: HttpResponse<IEtatCategorie[]>) => {
          this.isLoading = false;
          this.categoriserMatieres=res.body
        },
        () => {
          this.isLoading = false;
          this.onError();
        }
      );
  }

  ngOnInit() : void {
    this.getStat();
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }

}
