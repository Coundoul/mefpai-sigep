import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { InfrastructuresMatieresRoutingModule } from './route/infrastructures-maintenances-routing.module';
import { InfrastructuresMatieresComponent } from './list/infrastructures-maintenances.component';


@NgModule({
  declarations: [InfrastructuresMatieresComponent],
  imports: [
    CommonModule,
    InfrastructuresMatieresRoutingModule
  ]
})
export class InfrastructuresMatieresModule { }
