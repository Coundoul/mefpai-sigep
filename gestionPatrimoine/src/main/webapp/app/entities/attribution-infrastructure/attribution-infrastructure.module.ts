import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { AttributionInfrastructureComponent } from './list/attribution-infrastructure.component';
import { AttributionInfrastructureDetailComponent } from './detail/attribution-infrastructure-detail.component';
import { AttributionInfrastructureUpdateComponent } from './update/attribution-infrastructure-update.component';
import { AttributionInfrastructureDeleteDialogComponent } from './delete/attribution-infrastructure-delete-dialog.component';
import { AttributionInfrastructureRoutingModule } from './route/attribution-infrastructure-routing.module';
import { SidebarModule } from 'app/layouts/sidebar/sidebar.module';
import { EquipementModule } from '../equipement/equipement.module';

@NgModule({
  imports: [SharedModule, AttributionInfrastructureRoutingModule, SidebarModule, EquipementModule],
  declarations: [
    AttributionInfrastructureComponent,
    AttributionInfrastructureDetailComponent,
    AttributionInfrastructureUpdateComponent,
    AttributionInfrastructureDeleteDialogComponent,
  ],
  entryComponents: [AttributionInfrastructureDeleteDialogComponent],
})
export class AttributionInfrastructureModule {}
