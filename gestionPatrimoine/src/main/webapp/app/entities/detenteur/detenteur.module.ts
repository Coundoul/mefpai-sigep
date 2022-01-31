import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { DetenteurComponent } from './list/detenteur.component';
import { DetenteurDetailComponent } from './detail/detenteur-detail.component';
import { DetenteurUpdateComponent } from './update/detenteur-update.component';
import { DetenteurDeleteDialogComponent } from './delete/detenteur-delete-dialog.component';
import { DetenteurRoutingModule } from './route/detenteur-routing.module';
import { SidebarModule } from 'app/layouts/sidebar/sidebar.module';

@NgModule({
  imports: [SharedModule, DetenteurRoutingModule, SidebarModule],
  declarations: [DetenteurComponent, DetenteurDetailComponent, DetenteurUpdateComponent, DetenteurDeleteDialogComponent],
  entryComponents: [DetenteurDeleteDialogComponent],
})
export class DetenteurModule {}
