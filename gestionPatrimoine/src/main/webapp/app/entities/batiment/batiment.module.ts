import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { BatimentComponent } from './list/batiment.component';
import { BatimentDetailComponent } from './detail/batiment-detail.component';
import { BatimentUpdateComponent } from './update/batiment-update.component';
import { BatimentDeleteDialogComponent } from './delete/batiment-delete-dialog.component';
import { BatimentRoutingModule } from './route/batiment-routing.module';

@NgModule({
  imports: [SharedModule, BatimentRoutingModule],
  declarations: [BatimentComponent, BatimentDetailComponent, BatimentUpdateComponent, BatimentDeleteDialogComponent],
  entryComponents: [BatimentDeleteDialogComponent],
})
export class BatimentModule {}
