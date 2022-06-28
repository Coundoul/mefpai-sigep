import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ProjetAttributionComponent } from './list/projet-attribution.component';
import { ProjetAttributionDetailComponent } from './detail/projet-attribution-detail.component';
import { ProjetAttributionUpdateComponent } from './update/projet-attribution-update.component';
import { ProjetAttributionDeleteDialogComponent } from './delete/projet-attribution-delete-dialog.component';
import { ProjetAttributionRoutingModule } from './route/projet-attribution-routing.module';
import { SidebarModule } from 'app/layouts/sidebar/sidebar.module';

@NgModule({
  imports: [SharedModule, ProjetAttributionRoutingModule, SidebarModule],
  declarations: [
    ProjetAttributionComponent,
    ProjetAttributionDetailComponent,
    ProjetAttributionUpdateComponent,
    ProjetAttributionDeleteDialogComponent,
  ],
  entryComponents: [ProjetAttributionDeleteDialogComponent],
})
export class ProjetAttributionModule {}
