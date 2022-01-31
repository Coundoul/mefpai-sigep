import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ComptablePrincipaleComponent } from './list/comptable-principale.component';
import { ComptablePrincipaleDetailComponent } from './detail/comptable-principale-detail.component';
import { ComptablePrincipaleUpdateComponent } from './update/comptable-principale-update.component';
import { ComptablePrincipaleDeleteDialogComponent } from './delete/comptable-principale-delete-dialog.component';
import { ComptablePrincipaleRoutingModule } from './route/comptable-principale-routing.module';
import { SidebarModule } from 'app/layouts/sidebar/sidebar.module';

@NgModule({
  imports: [SharedModule, ComptablePrincipaleRoutingModule, SidebarModule],
  declarations: [
    ComptablePrincipaleComponent,
    ComptablePrincipaleDetailComponent,
    ComptablePrincipaleUpdateComponent,
    ComptablePrincipaleDeleteDialogComponent,
  ],
  entryComponents: [ComptablePrincipaleDeleteDialogComponent],
})
export class ComptablePrincipaleModule {}
