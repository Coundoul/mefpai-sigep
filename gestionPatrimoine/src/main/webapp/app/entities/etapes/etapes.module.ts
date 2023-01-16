import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { EtapesComponent } from './list/etapes.component';
import { EtapesDetailComponent } from './detail/etapes-detail.component';
import { EtapesUpdateComponent } from './update/etapes-update.component';
import { EtapesDeleteDialogComponent } from './delete/etapes-delete-dialog.component';
import { EtapesRoutingModule } from './route/etapes-routing.module';
import { SidebarModule } from 'app/layouts/sidebar/sidebar.module';
import {FullCalendarModule} from 'primeng-lts/fullcalendar';




@NgModule({
  imports: [SharedModule, EtapesRoutingModule, SidebarModule, FullCalendarModule],
  declarations: [EtapesComponent, EtapesDetailComponent, EtapesUpdateComponent, EtapesDeleteDialogComponent],
  entryComponents: [EtapesDeleteDialogComponent],
})
export class EtapesModule {}
