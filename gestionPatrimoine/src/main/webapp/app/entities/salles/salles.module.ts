import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { SallesComponent } from './list/salles.component';
import { SallesDetailComponent } from './detail/salles-detail.component';
import { SallesUpdateComponent } from './update/salles-update.component';
import { SallesDeleteDialogComponent } from './delete/salles-delete-dialog.component';
import { SallesRoutingModule } from './route/salles-routing.module';

@NgModule({
  imports: [SharedModule, SallesRoutingModule],
  declarations: [SallesComponent, SallesDetailComponent, SallesUpdateComponent, SallesDeleteDialogComponent],
  entryComponents: [SallesDeleteDialogComponent],
})
export class SallesModule {}
