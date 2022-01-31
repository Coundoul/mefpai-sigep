import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { FiliereStabiliseComponent } from './list/filiere-stabilise.component';
import { FiliereStabiliseDetailComponent } from './detail/filiere-stabilise-detail.component';
import { FiliereStabiliseUpdateComponent } from './update/filiere-stabilise-update.component';
import { FiliereStabiliseDeleteDialogComponent } from './delete/filiere-stabilise-delete-dialog.component';
import { FiliereStabiliseRoutingModule } from './route/filiere-stabilise-routing.module';

@NgModule({
  imports: [SharedModule, FiliereStabiliseRoutingModule],
  declarations: [
    FiliereStabiliseComponent,
    FiliereStabiliseDetailComponent,
    FiliereStabiliseUpdateComponent,
    FiliereStabiliseDeleteDialogComponent,
  ],
  entryComponents: [FiliereStabiliseDeleteDialogComponent],
})
export class FiliereStabiliseModule {}
