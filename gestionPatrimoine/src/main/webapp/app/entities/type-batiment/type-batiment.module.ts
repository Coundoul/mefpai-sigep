import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { TypeBatimentComponent } from './list/type-batiment.component';
import { TypeBatimentDetailComponent } from './detail/type-batiment-detail.component';
import { TypeBatimentUpdateComponent } from './update/type-batiment-update.component';
import { TypeBatimentDeleteDialogComponent } from './delete/type-batiment-delete-dialog.component';
import { TypeBatimentRoutingModule } from './route/type-batiment-routing.module';

@NgModule({
  imports: [SharedModule, TypeBatimentRoutingModule],
  declarations: [TypeBatimentComponent, TypeBatimentDetailComponent, TypeBatimentUpdateComponent, TypeBatimentDeleteDialogComponent],
  entryComponents: [TypeBatimentDeleteDialogComponent],
})
export class TypeBatimentModule {}
