import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ProjetsComponent } from './list/projets.component';
import { ProjetsDetailComponent } from './detail/projets-detail.component';
import { ProjetsUpdateComponent } from './update/projets-update.component';
import { ProjetsDeleteDialogComponent } from './delete/projets-delete-dialog.component';
import { ProjetsRoutingModule } from './route/projets-routing.module';
import { SidebarModule } from 'app/layouts/sidebar/sidebar.module';

@NgModule({
  imports: [SharedModule, ProjetsRoutingModule, SidebarModule],
  declarations: [ProjetsComponent, ProjetsDetailComponent, ProjetsUpdateComponent, ProjetsDeleteDialogComponent],
  entryComponents: [ProjetsDeleteDialogComponent],
})
export class ProjetsModule {}
