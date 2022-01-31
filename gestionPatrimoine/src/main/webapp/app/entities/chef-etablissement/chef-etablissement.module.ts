import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ChefEtablissementComponent } from './list/chef-etablissement.component';
import { ChefEtablissementDetailComponent } from './detail/chef-etablissement-detail.component';
import { ChefEtablissementUpdateComponent } from './update/chef-etablissement-update.component';
import { ChefEtablissementDeleteDialogComponent } from './delete/chef-etablissement-delete-dialog.component';
import { ChefEtablissementRoutingModule } from './route/chef-etablissement-routing.module';

@NgModule({
  imports: [SharedModule, ChefEtablissementRoutingModule],
  declarations: [
    ChefEtablissementComponent,
    ChefEtablissementDetailComponent,
    ChefEtablissementUpdateComponent,
    ChefEtablissementDeleteDialogComponent,
  ],
  entryComponents: [ChefEtablissementDeleteDialogComponent],
})
export class ChefEtablissementModule {}
