import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { BonComponent } from './list/bon.component';
import { BonDetailComponent } from './detail/bon-detail.component';
import { BonUpdateComponent } from './update/bon-update.component';
import { BonDeleteDialogComponent } from './delete/bon-delete-dialog.component';
import { BonRoutingModule } from './route/bon-routing.module';
import { SidebarModule } from 'app/layouts/sidebar/sidebar.module';
import { MatStepperModule } from '@angular/material/stepper';
import { AffectationsModule } from '../affectations/affectations.module';
import { AffectationsUpdateComponent } from '../affectations/update/affectations-update.component';

@NgModule({
  imports: [SharedModule, BonRoutingModule, SidebarModule, MatStepperModule, AffectationsModule],
  declarations: [BonComponent, BonDetailComponent, BonDeleteDialogComponent, BonUpdateComponent, AffectationsUpdateComponent],
  entryComponents: [BonDeleteDialogComponent],
})
export class BonModule {}
