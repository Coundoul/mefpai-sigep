import { NgModule } from '@angular/core';
import { InventaireComponent } from './inventaire.component';
import { RouterModule } from '@angular/router';
import { INVENTAIRE_ROUTE } from './inventaire.route';
import { SidebarModule } from 'app/layouts/sidebar/sidebar.module';
import { SharedModule } from 'app/shared/shared.module';
import { MatDialogModule } from '@angular/material/dialog';
import { DetailInventaireComponent } from './detail-inventaire/detail-inventaire.component';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';

@NgModule({
  imports: [
    RouterModule.forChild([INVENTAIRE_ROUTE]),
    SidebarModule,
    SharedModule,
    MatDialogModule,
    MatButtonModule,
    CommonModule,
  ],
  declarations: [InventaireComponent, DetailInventaireComponent],
  //entryComponents: [DetailInventaireComponent],
})
export class InventaireModule { }
