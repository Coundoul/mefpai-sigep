import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ChefProjetComponent } from './list/chef-projet.component';
import { ChefProjetDetailComponent } from './detail/chef-projet-detail.component';
import { ChefProjetUpdateComponent } from './update/chef-projet-update.component';
import { ChefProjetDeleteDialogComponent } from './delete/chef-projet-delete-dialog.component';
import { ChefProjetRoutingModule } from './route/chef-projet-routing.module';

@NgModule({
  imports: [SharedModule, ChefProjetRoutingModule],
  declarations: [ChefProjetComponent, ChefProjetDetailComponent, ChefProjetUpdateComponent, ChefProjetDeleteDialogComponent],
  entryComponents: [ChefProjetDeleteDialogComponent],
})
export class ChefProjetModule {}
