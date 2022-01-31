import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ResponsableComponent } from './list/responsable.component';
import { ResponsableDetailComponent } from './detail/responsable-detail.component';
import { ResponsableUpdateComponent } from './update/responsable-update.component';
import { ResponsableDeleteDialogComponent } from './delete/responsable-delete-dialog.component';
import { ResponsableRoutingModule } from './route/responsable-routing.module';

@NgModule({
  imports: [SharedModule, ResponsableRoutingModule],
  declarations: [ResponsableComponent, ResponsableDetailComponent, ResponsableUpdateComponent, ResponsableDeleteDialogComponent],
  entryComponents: [ResponsableDeleteDialogComponent],
})
export class ResponsableModule {}
