import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { BureauComponent } from './list/bureau.component';
import { BureauDetailComponent } from './detail/bureau-detail.component';
import { BureauUpdateComponent } from './update/bureau-update.component';
import { BureauDeleteDialogComponent } from './delete/bureau-delete-dialog.component';
import { BureauRoutingModule } from './route/bureau-routing.module';

@NgModule({
  imports: [SharedModule, BureauRoutingModule],
  declarations: [BureauComponent, BureauDetailComponent, BureauUpdateComponent, BureauDeleteDialogComponent],
  entryComponents: [BureauDeleteDialogComponent],
})
export class BureauModule {}
