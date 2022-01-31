import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { FicheTechniqueComponent } from './list/fiche-technique.component';
import { FicheTechniqueDetailComponent } from './detail/fiche-technique-detail.component';
import { FicheTechniqueUpdateComponent } from './update/fiche-technique-update.component';
import { FicheTechniqueDeleteDialogComponent } from './delete/fiche-technique-delete-dialog.component';
import { FicheTechniqueRoutingModule } from './route/fiche-technique-routing.module';
import { SidebarModule } from 'app/layouts/sidebar/sidebar.module';

@NgModule({
  imports: [SharedModule, FicheTechniqueRoutingModule, SidebarModule,],
  declarations: [
    FicheTechniqueComponent,
    FicheTechniqueDetailComponent,
    FicheTechniqueUpdateComponent,
    FicheTechniqueDeleteDialogComponent,
  ],
  entryComponents: [FicheTechniqueDeleteDialogComponent],
})
export class FicheTechniqueModule {}
