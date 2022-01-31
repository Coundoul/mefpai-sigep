import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { CategorieMatiereComponent } from './list/categorie-matiere.component';
import { CategorieMatiereDetailComponent } from './detail/categorie-matiere-detail.component';
import { CategorieMatiereUpdateComponent } from './update/categorie-matiere-update.component';
import { CategorieMatiereDeleteDialogComponent } from './delete/categorie-matiere-delete-dialog.component';
import { CategorieMatiereRoutingModule } from './route/categorie-matiere-routing.module';
import { SidebarModule } from 'app/layouts/sidebar/sidebar.module';

@NgModule({
  imports: [SharedModule, CategorieMatiereRoutingModule, SidebarModule],
  declarations: [
    CategorieMatiereComponent,
    CategorieMatiereDetailComponent,
    CategorieMatiereUpdateComponent,
    CategorieMatiereDeleteDialogComponent,
  ],
  entryComponents: [CategorieMatiereDeleteDialogComponent],
})
export class CategorieMatiereModule {}
