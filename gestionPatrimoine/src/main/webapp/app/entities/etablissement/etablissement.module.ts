import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { EtablissementComponent } from './list/etablissement.component';
import { EtablissementDetailComponent } from './detail/etablissement-detail.component';
import { EtablissementUpdateComponent } from './update/etablissement-update.component';
import { EtablissementDeleteDialogComponent } from './delete/etablissement-delete-dialog.component';
import { EtablissementRoutingModule } from './route/etablissement-routing.module';
import { SidebarModule } from 'app/layouts/sidebar/sidebar.module';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { MatStepperModule } from '@angular/material/stepper';
import { MatButtonModule } from '@angular/material/button';
import { GMapModule } from 'primeng-lts/gmap';
import { ToastModule } from 'primeng-lts/toast';
import { CheckboxModule } from 'primeng-lts/checkbox';
import { ButtonModule } from 'primeng-lts/button';
import { DialogModule } from 'primeng-lts/dialog';
import { MessageService } from 'primeng-lts/api';
import { EtatinfraComponent } from './etatinfra/etatinfra.component';

@NgModule({
  imports: [SharedModule, 
    ToastModule,
    GMapModule,
    CheckboxModule,
    ButtonModule,
    DialogModule,
    EtablissementRoutingModule,
    MatButtonModule, 
    SidebarModule, 
    MatStepperModule, 
    ReactiveFormsModule, 
    FormsModule],
  providers: [MessageService],
  declarations: [EtablissementComponent, EtablissementDetailComponent, EtablissementUpdateComponent, EtablissementDeleteDialogComponent, EtatinfraComponent],
  entryComponents: [EtablissementDeleteDialogComponent],
})
export class EtablissementModule {}
