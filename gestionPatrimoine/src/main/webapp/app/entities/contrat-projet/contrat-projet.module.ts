import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ContratProjetComponent } from './list/contrat-projet.component';
import { ContratProjetDetailComponent } from './detail/contrat-projet-detail.component';
import { ContratProjetUpdateComponent } from './update/contrat-projet-update.component';
import { ContratProjetDeleteDialogComponent } from './delete/contrat-projet-delete-dialog.component';
import { ContratProjetRoutingModule } from './route/contrat-projet-routing.module';

@NgModule({
  imports: [SharedModule, ContratProjetRoutingModule],
  declarations: [ContratProjetComponent, ContratProjetDetailComponent, ContratProjetUpdateComponent, ContratProjetDeleteDialogComponent],
  entryComponents: [ContratProjetDeleteDialogComponent],
})
export class ContratProjetModule {}
