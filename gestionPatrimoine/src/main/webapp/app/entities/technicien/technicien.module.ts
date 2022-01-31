import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { TechnicienComponent } from './list/technicien.component';
import { TechnicienDetailComponent } from './detail/technicien-detail.component';
import { TechnicienUpdateComponent } from './update/technicien-update.component';
import { TechnicienDeleteDialogComponent } from './delete/technicien-delete-dialog.component';
import { TechnicienRoutingModule } from './route/technicien-routing.module';
import { SidebarModule } from 'app/layouts/sidebar/sidebar.module';

@NgModule({
  imports: [SharedModule, TechnicienRoutingModule, SidebarModule],
  declarations: [TechnicienComponent, TechnicienDetailComponent, TechnicienUpdateComponent, TechnicienDeleteDialogComponent],
  entryComponents: [TechnicienDeleteDialogComponent],
})
export class TechnicienModule {}
