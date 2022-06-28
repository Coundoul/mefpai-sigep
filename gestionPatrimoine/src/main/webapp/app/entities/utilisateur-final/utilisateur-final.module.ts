import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { UtilisateurFinalComponent } from './list/utilisateur-final.component';
import { UtilisateurFinalDetailComponent } from './detail/utilisateur-final-detail.component';
import { UtilisateurFinalUpdateComponent } from './update/utilisateur-final-update.component';
import { UtilisateurFinalDeleteDialogComponent } from './delete/utilisateur-final-delete-dialog.component';
import { UtilisateurFinalRoutingModule } from './route/utilisateur-final-routing.module';

@NgModule({
  imports: [SharedModule, UtilisateurFinalRoutingModule],
  declarations: [
    UtilisateurFinalComponent,
    UtilisateurFinalDetailComponent,
    UtilisateurFinalUpdateComponent,
    UtilisateurFinalDeleteDialogComponent,
  ],
  entryComponents: [UtilisateurFinalDeleteDialogComponent],
})
export class UtilisateurFinalModule {}
