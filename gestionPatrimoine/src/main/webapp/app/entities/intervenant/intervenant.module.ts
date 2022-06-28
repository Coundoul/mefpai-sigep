import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { IntervenantComponent } from './list/intervenant.component';
import { IntervenantDetailComponent } from './detail/intervenant-detail.component';
import { IntervenantUpdateComponent } from './update/intervenant-update.component';
import { IntervenantDeleteDialogComponent } from './delete/intervenant-delete-dialog.component';
import { IntervenantRoutingModule } from './route/intervenant-routing.module';
import { SidebarModule } from 'app/layouts/sidebar/sidebar.module';

@NgModule({
  imports: [SharedModule, IntervenantRoutingModule, SidebarModule],
  declarations: [IntervenantComponent, IntervenantDetailComponent, IntervenantUpdateComponent, IntervenantDeleteDialogComponent],
  entryComponents: [IntervenantDeleteDialogComponent],
})
export class IntervenantModule {}
