import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ComptableSecondaireComponent } from './list/comptable-secondaire.component';
import { ComptableSecondaireDetailComponent } from './detail/comptable-secondaire-detail.component';
import { ComptableSecondaireUpdateComponent } from './update/comptable-secondaire-update.component';
import { ComptableSecondaireDeleteDialogComponent } from './delete/comptable-secondaire-delete-dialog.component';
import { ComptableSecondaireRoutingModule } from './route/comptable-secondaire-routing.module';
import { SidebarModule } from 'app/layouts/sidebar/sidebar.module';

@NgModule({
  imports: [SharedModule, ComptableSecondaireRoutingModule, SidebarModule],
  declarations: [
    ComptableSecondaireComponent,
    ComptableSecondaireDetailComponent,
    ComptableSecondaireUpdateComponent,
    ComptableSecondaireDeleteDialogComponent,
  ],
  entryComponents: [ComptableSecondaireDeleteDialogComponent],
})
export class ComptableSecondaireModule {}
