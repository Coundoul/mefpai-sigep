import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { DetailSortieComponent } from './list/detail-sortie.component';
import { DetailSortieDetailComponent } from './detail/detail-sortie-detail.component';
import { DetailSortieUpdateComponent } from './update/detail-sortie-update.component';
import { DetailSortieDeleteDialogComponent } from './delete/detail-sortie-delete-dialog.component';
import { DetailSortieRoutingModule } from './route/detail-sortie-routing.module';

@NgModule({
  imports: [SharedModule, DetailSortieRoutingModule],
  declarations: [DetailSortieComponent, DetailSortieDetailComponent, DetailSortieUpdateComponent, DetailSortieDeleteDialogComponent],
  entryComponents: [DetailSortieDeleteDialogComponent],
})
export class DetailSortieModule {}
