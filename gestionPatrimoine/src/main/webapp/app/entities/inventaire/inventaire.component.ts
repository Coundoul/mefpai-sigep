import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import {  NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { DataUtils } from 'app/core/util/data-util.service';
import { DetailInventaireComponent } from './detail-inventaire/detail-inventaire.component';
import { IInventaire } from './inventaire.model';
import { InventaireService } from './inventaire.service';


@Component({
  selector: 'jhi-inventaire',
  templateUrl: './inventaire.component.html',
  styleUrls: ['./inventaire.component.scss']
})
export class InventaireComponent implements OnInit {
  inventaires:any;
  inventaire:any;
  detailInventaire:any;
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
    protected inventaireService : InventaireService,
    public dialog: MatDialog,
  ) { }
  
  getStat():void{
    this.inventaireService
      .listtableauInventaire()
      .subscribe(
        (res: HttpResponse<IInventaire[]>) => {
          this.isLoading = false;
          this.inventaires=res.body
        },
        () => {
          this.isLoading = false;
          this.onError();
        }
      );
  }

  getStatDetail(ref: any):void{
    this.inventaireService
      .detail(ref)
      .subscribe(
        (res: HttpResponse<any>) => {
          this.isLoading = false;
          this.detailInventaire=res.body;
          //alert(this.detailInventaire);
        },
        () => {
          this.isLoading = false;
        }
      );
  }



  ngOnInit() : void {
    this.getStat();
  }

  detail(inventaire: any): void {
    this.getStatDetail(inventaire);
    const modalRef = this.modalService.open(DetailInventaireComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.detailInventaire = this.detailInventaire;
  }
  
  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
