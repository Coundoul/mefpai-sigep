import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { FicheTechniqueMaintenanceComponent } from './list/fiche-technique-maintenance.component';
import { FicheTechniqueMaintenanceDetailComponent } from './detail/fiche-technique-maintenance-detail.component';
import { FicheTechniqueMaintenanceUpdateComponent } from './update/fiche-technique-maintenance-update.component';
import { FicheTechniqueMaintenanceDeleteDialogComponent } from './delete/fiche-technique-maintenance-delete-dialog.component';
import { FicheTechniqueMaintenanceRoutingModule } from './route/fiche-technique-maintenance-routing.module';

@NgModule({
  imports: [SharedModule, FicheTechniqueMaintenanceRoutingModule],
  declarations: [
    FicheTechniqueMaintenanceComponent,
    FicheTechniqueMaintenanceDetailComponent,
    FicheTechniqueMaintenanceUpdateComponent,
    FicheTechniqueMaintenanceDeleteDialogComponent,
  ],
  entryComponents: [FicheTechniqueMaintenanceDeleteDialogComponent],
})
export class FicheTechniqueMaintenanceModule {}
