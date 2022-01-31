import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { NatureFoncierComponent } from './list/nature-foncier.component';
import { NatureFoncierDetailComponent } from './detail/nature-foncier-detail.component';
import { NatureFoncierUpdateComponent } from './update/nature-foncier-update.component';
import { NatureFoncierDeleteDialogComponent } from './delete/nature-foncier-delete-dialog.component';
import { NatureFoncierRoutingModule } from './route/nature-foncier-routing.module';

@NgModule({
  imports: [SharedModule, NatureFoncierRoutingModule],
  declarations: [NatureFoncierComponent, NatureFoncierDetailComponent, NatureFoncierUpdateComponent, NatureFoncierDeleteDialogComponent],
  entryComponents: [NatureFoncierDeleteDialogComponent],
})
export class NatureFoncierModule {}
