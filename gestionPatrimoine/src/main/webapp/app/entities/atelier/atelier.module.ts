import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { AtelierComponent } from './list/atelier.component';
import { AtelierDetailComponent } from './detail/atelier-detail.component';
import { AtelierUpdateComponent } from './update/atelier-update.component';
import { AtelierDeleteDialogComponent } from './delete/atelier-delete-dialog.component';
import { AtelierRoutingModule } from './route/atelier-routing.module';

@NgModule({
  imports: [SharedModule, AtelierRoutingModule],
  declarations: [AtelierComponent, AtelierDetailComponent, AtelierUpdateComponent, AtelierDeleteDialogComponent],
  entryComponents: [AtelierDeleteDialogComponent],
})
export class AtelierModule {}
