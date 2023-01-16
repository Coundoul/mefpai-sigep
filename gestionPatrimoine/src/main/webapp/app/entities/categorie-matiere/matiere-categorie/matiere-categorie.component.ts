import { HttpResponse, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Validators, FormBuilder } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { DataUtils } from 'app/core/util/data-util.service';
import { IEquipement } from 'app/entities/equipement/equipement.model';
import { EquipementService } from 'app/entities/equipement/service/equipement.service';
import { combineLatest } from 'rxjs';
import { CategorieMatiereService } from '../service/categorie-matiere.service';

@Component({
  selector: 'jhi-matiere-categorie',
  templateUrl: './matiere-categorie.component.html',
  styleUrls: ['./matiere-categorie.component.scss']
})
export class MatiereCategorieComponent implements OnInit {

  isSaving = false;
  equipements?: IEquipement[];
  categorieMatiere: any;
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  recherche: any;
  ascending!: boolean;
  ngbPaginationPage = 1;
  detailRecherche: any;
  sub: any;


  editForm = this.fb.group({
    recherche: [null, [Validators.required]],
  });

  constructor(
    protected equipementService: EquipementService,
    protected categorieMatiereService: CategorieMatiereService,
    protected activatedRoute: ActivatedRoute,
    protected dataUtils: DataUtils,
    protected router: Router,
    protected modalService: NgbModal,
    protected fb: FormBuilder
  ) {}


  
  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;
    
    this.categorieMatiereService
      .queryMatiere(this.categorieMatiere, {page: pageToLoad - 1, size: this.itemsPerPage, sort: this.sort(),})
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

    this.activatedRoute.data.subscribe(() => {
      this.categorieMatiere = Number(this.activatedRoute.snapshot.paramMap.get('id'));
    });
    this.handleNavigation();

    // this.sub = this.activatedRoute.params.subscribe(params => {
    //   this.categorieMatiere = Number(this.activatedRoute.snapshot.paramMap.get('id'));
    //   if (this.categorieMatiere !== undefined){
    //     alert("Bonjour 1111");
    //     alert(this.handleNavigation());
    //   }
    // });
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

  // delete(equipement: IEquipement): void {
  //   const modalRef = this.modalService.open(EquipementDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
  //   modalRef.componentInstance.equipement = equipement;
  //   // unsubscribe not needed because closed completes on modal close
  //   modalRef.closed.subscribe(reason => {
  //     if (reason === 'deleted') {
  //       this.loadPage();
  //     }
  //   });
  // }

  // getStatDetail(ref: any): void {
  //   this.equipementService.rechercher(ref).subscribe(
  //     (res: HttpResponse<any>) => {
  //       this.isLoading = false;
  //       this.detailRecherche = res.body;
  //     },
  //     () => {
  //       this.isLoading = false;
  //     }
  //   );
  // }

  // rechercher(): void {
  //   this.isSaving = true;
  //   this.recherche = this.editForm.get(['recherche'])!.value;
  //   this.router.navigate(['/equipement/search', { reference: this.recherche }]);
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
      alert("Bonjour 2222");
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        alert("Bonjour 3333");
        this.predicate = predicate;
        this.ascending = ascending;
        this.predicate;
        this.ascending;
        this.loadPage(pageNumber, true);
      }
    });
  }

  protected onSuccess(data: IEquipement[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/categorie-matiere/', { id: this.categorieMatiere}, '/matieres'], {
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
