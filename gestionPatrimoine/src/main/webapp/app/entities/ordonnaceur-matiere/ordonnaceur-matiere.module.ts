import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { OrdonnaceurMatiereComponent } from './list/ordonnaceur-matiere.component';
import { OrdonnaceurMatiereDetailComponent } from './detail/ordonnaceur-matiere-detail.component';
import { OrdonnaceurMatiereUpdateComponent } from './update/ordonnaceur-matiere-update.component';
import { OrdonnaceurMatiereDeleteDialogComponent } from './delete/ordonnaceur-matiere-delete-dialog.component';
import { OrdonnaceurMatiereRoutingModule } from './route/ordonnaceur-matiere-routing.module';
import { SidebarModule } from 'app/layouts/sidebar/sidebar.module';

@NgModule({
  imports: [SharedModule, OrdonnaceurMatiereRoutingModule, SidebarModule],
  declarations: [
    OrdonnaceurMatiereComponent,
    OrdonnaceurMatiereDetailComponent,
    OrdonnaceurMatiereUpdateComponent,
    OrdonnaceurMatiereDeleteDialogComponent,
  ],
  entryComponents: [OrdonnaceurMatiereDeleteDialogComponent],
})
export class OrdonnaceurMatiereModule {}
