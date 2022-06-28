import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { CorpsEtatComponent } from './list/corps-etat.component';
import { CorpsEtatDetailComponent } from './detail/corps-etat-detail.component';
import { CorpsEtatUpdateComponent } from './update/corps-etat-update.component';
import { CorpsEtatDeleteDialogComponent } from './delete/corps-etat-delete-dialog.component';
import { CorpsEtatRoutingModule } from './route/corps-etat-routing.module';

@NgModule({
  imports: [SharedModule, CorpsEtatRoutingModule],
  declarations: [CorpsEtatComponent, CorpsEtatDetailComponent, CorpsEtatUpdateComponent, CorpsEtatDeleteDialogComponent],
  entryComponents: [CorpsEtatDeleteDialogComponent],
})
export class CorpsEtatModule {}
