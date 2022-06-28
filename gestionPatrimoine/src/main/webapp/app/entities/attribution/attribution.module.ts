import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { AttributionComponent } from './list/attribution.component';
import { AttributionDetailComponent } from './detail/attribution-detail.component';
import { AttributionDeleteDialogComponent } from './delete/attribution-delete-dialog.component';
import { AttributionRoutingModule } from './route/attribution-routing.module';
import { AttributionUpdateComponent } from './update/attribution-update.component';
import { SidebarComponent } from 'app/layouts/sidebar/sidebar.component';
import { SidebarModule } from 'app/layouts/sidebar/sidebar.module';

@NgModule({
  imports: [SharedModule, AttributionRoutingModule, SidebarModule],
  declarations: [AttributionComponent, AttributionDetailComponent, AttributionDeleteDialogComponent, AttributionUpdateComponent],
  entryComponents: [AttributionDeleteDialogComponent],
})
export class AttributionModule {}
