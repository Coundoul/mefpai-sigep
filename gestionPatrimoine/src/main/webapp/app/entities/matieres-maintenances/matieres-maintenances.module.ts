import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MatieresMaintenancesRoutingModule } from './route/matieres-maintenances-routing.module';
import { MatieresMaintenancesComponent } from './list/matieres-maintenances.component';
import { AgmCoreModule } from '@agm/core';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatStepperModule } from '@angular/material/stepper';
import { MatCarouselModule } from '@ngmodule/material-carousel';
import { SidebarModule } from 'app/layouts/sidebar/sidebar.module';
import { SharedModule } from 'primeng-lts/api';
import { CarouselModule } from 'primeng-lts/carousel';
import { DataViewModule } from 'primeng-lts/dataview';


@NgModule({
  imports: [
    CommonModule,
    MatieresMaintenancesRoutingModule,
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
  declarations: [MatieresMaintenancesComponent]
})
export class MatieresMaintenancesModule { }

