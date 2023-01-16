import { Component, OnInit } from '@angular/core';

import { LoginService } from 'app/login/login.service';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { TreeNode } from 'primeng-lts/api';
import { MessageService } from 'primeng-lts/api';
import { EquipementService } from 'app/entities/equipement/service/equipement.service';
import { IEquipement } from 'app/entities/equipement/equipement.model';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { Router } from '@angular/router';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  providers: [MessageService],
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit {
  account: Account | null = null;

  data!: TreeNode[];

  selectedNode!: TreeNode;

  dataStatics: any;

  donneesStatic: any;

  listeCouleur: any;

  present: any;

  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  ascending!: boolean;
  ngbPaginationPage = 1;
  predicate!: string;

  constructor(private accountService: AccountService, protected router: Router, protected equipementService: EquipementService,private messageService: MessageService, private loginService: LoginService) {
    this.dataStatics = {
      datasets: [{
          data: [
              11,
              16,
              7,
              3,
              14
          ],
          backgroundColor: [
              "#FF6384",
              "#4BC0C0",
              "#FFCE56",
              "#E7E9ED",
              "#36A2EB"
          ],
          label: 'My dataset'
      }],
      labels: [
          "Red",
          "Green",
          "Yellow",
          "Grey",
          "Blue"
      ]
  }
  }

  loadStaticMatiere(): void {
    this.equipementService.queryStaticMatiere().subscribe(
      (res: HttpResponse<any>) => {
       this.isLoading = false;
       this.donneesStatic = res.body;
      },
      () => {
        this.isLoading = false;
        this.onError();
      }
    );
  }

  ngOnInit(): void {
    //alert(this.donneesStatic);
    this.accountService.identity().subscribe(account => (this.account = account));
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }

  login(): void {
    this.loginService.login();
  }

  onNodeSelect(event) :void{
    this.messageService.add({ severity: 'success', summary: 'Node Selected', detail: event.node.label });
  }

  protected onSuccess(data: IEquipement[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/']);
    }
    this.listeCouleur = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }

  
  
}
