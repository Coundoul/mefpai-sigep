import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { RequeteComponent } from './list/requete.component';
import { RequeteDetailComponent } from './detail/requete-detail.component';
import { RequeteUpdateComponent } from './update/requete-update.component';
import { RequeteDeleteDialogComponent } from './delete/requete-delete-dialog.component';
import { RequeteRoutingModule } from './route/requete-routing.module';
import { SidebarModule } from 'app/layouts/sidebar/sidebar.module';

@NgModule({
  imports: [SharedModule, RequeteRoutingModule, SidebarModule],
  declarations: [RequeteComponent, RequeteDetailComponent, RequeteUpdateComponent, RequeteDeleteDialogComponent],
  entryComponents: [RequeteDeleteDialogComponent],
})
export class RequeteModule {}
