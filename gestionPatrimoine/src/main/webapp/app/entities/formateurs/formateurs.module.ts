import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { FormateursComponent } from './list/formateurs.component';
import { FormateursDetailComponent } from './detail/formateurs-detail.component';
import { FormateursUpdateComponent } from './update/formateurs-update.component';
import { FormateursDeleteDialogComponent } from './delete/formateurs-delete-dialog.component';
import { FormateursRoutingModule } from './route/formateurs-routing.module';

@NgModule({
  imports: [SharedModule, FormateursRoutingModule],
  declarations: [FormateursComponent, FormateursDetailComponent, FormateursUpdateComponent, FormateursDeleteDialogComponent],
  entryComponents: [FormateursDeleteDialogComponent],
})
export class FormateursModule {}
