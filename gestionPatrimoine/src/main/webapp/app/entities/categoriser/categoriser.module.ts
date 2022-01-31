import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CategoriserComponent } from './categoriser.component';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog';
import { SidebarModule } from 'app/layouts/sidebar/sidebar.module';
import { SharedModule } from 'app/shared/shared.module';
import { RouterModule } from '@angular/router';
import { CATEGORISER_ROUTE } from './categoriser.route';

@NgModule({
  imports: [
    RouterModule.forChild([CATEGORISER_ROUTE]),
    CommonModule,
    SidebarModule,
    SharedModule,
    MatDialogModule,
    MatButtonModule,
    CommonModule,
  ],
  declarations: [CategoriserComponent]
})
export class CategoriserModule { }
