import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ChefMaintenanceComponent } from './list/chef-maintenance.component';
import { ChefMaintenanceDetailComponent } from './detail/chef-maintenance-detail.component';
import { ChefMaintenanceUpdateComponent } from './update/chef-maintenance-update.component';
import { ChefMaintenanceDeleteDialogComponent } from './delete/chef-maintenance-delete-dialog.component';
import { ChefMaintenanceRoutingModule } from './route/chef-maintenance-routing.module';

@NgModule({
  imports: [SharedModule, ChefMaintenanceRoutingModule],
  declarations: [
    ChefMaintenanceComponent,
    ChefMaintenanceDetailComponent,
    ChefMaintenanceUpdateComponent,
    ChefMaintenanceDeleteDialogComponent,
  ],
  entryComponents: [ChefMaintenanceDeleteDialogComponent],
})
export class ChefMaintenanceModule {}
