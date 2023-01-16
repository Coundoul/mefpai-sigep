import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { EquipementComponent } from './list/equipement.component';
import { EquipementDetailComponent } from './detail/equipement-detail.component';
import { EquipementDeleteDialogComponent } from './delete/equipement-delete-dialog.component';
import { EquipementRoutingModule } from './route/equipement-routing.module';
import { SidebarModule } from 'app/layouts/sidebar/sidebar.module';
import { MatStepperModule } from '@angular/material/stepper';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { EquipementUpdateComponent } from './update/equipement-update.component';
import { SignalerComponent } from './signaler/signaler.component';
import { AgmCoreModule } from '@agm/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { RechercheEquipementComponent } from './recherche-equipement/recherche-equipement.component';
import { MatIconModule } from '@angular/material/icon';
import { MatCarouselModule } from '@ngmodule/material-carousel'; 
import {DataViewModule} from 'primeng-lts/dataview';
import {CarouselModule} from 'primeng-lts/carousel';

@NgModule({
  imports: [
    SharedModule,
    EquipementRoutingModule,
    SidebarModule,
    MatStepperModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    ReactiveFormsModule,
    FormsModule,
    CarouselModule,
    DataViewModule,
    MatCarouselModule.forRoot(),
    AgmCoreModule.forRoot({
      apiKey: 'YOUR_GOOGLE_MAPS_API_KEY',
    }),
  ],
  declarations: [
    EquipementComponent,
    EquipementDetailComponent,
    EquipementDeleteDialogComponent,
    RechercheEquipementComponent,
    EquipementUpdateComponent,
    SignalerComponent,
  ],
  entryComponents: [EquipementDeleteDialogComponent],
})
export class EquipementModule {}
