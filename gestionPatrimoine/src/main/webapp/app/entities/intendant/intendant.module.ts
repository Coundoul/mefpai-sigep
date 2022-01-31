import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { IntendantComponent } from './list/intendant.component';
import { IntendantDetailComponent } from './detail/intendant-detail.component';
import { IntendantUpdateComponent } from './update/intendant-update.component';
import { IntendantDeleteDialogComponent } from './delete/intendant-delete-dialog.component';
import { IntendantRoutingModule } from './route/intendant-routing.module';

@NgModule({
  imports: [SharedModule, IntendantRoutingModule],
  declarations: [IntendantComponent, IntendantDetailComponent, IntendantUpdateComponent, IntendantDeleteDialogComponent],
  entryComponents: [IntendantDeleteDialogComponent],
})
export class IntendantModule {}
