import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { AffectationsComponent } from './list/affectations.component';
import { AffectationsDetailComponent } from './detail/affectations-detail.component';
import { AffectationsDeleteDialogComponent } from './delete/affectations-delete-dialog.component';
import { AffectationsRoutingModule } from './route/affectations-routing.module';
import { SidebarModule } from 'app/layouts/sidebar/sidebar.module';
import { UserService } from '../user/user.service';

@NgModule({
  imports: [SharedModule, AffectationsRoutingModule, SidebarModule],
  providers:[UserService],
  declarations: [AffectationsComponent, AffectationsDetailComponent, AffectationsDeleteDialogComponent],
  entryComponents: [AffectationsDeleteDialogComponent],
})
export class AffectationsModule {}
