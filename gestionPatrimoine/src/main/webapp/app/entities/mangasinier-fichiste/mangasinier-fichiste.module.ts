import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { MangasinierFichisteComponent } from './list/mangasinier-fichiste.component';
import { MangasinierFichisteDetailComponent } from './detail/mangasinier-fichiste-detail.component';
import { MangasinierFichisteUpdateComponent } from './update/mangasinier-fichiste-update.component';
import { MangasinierFichisteDeleteDialogComponent } from './delete/mangasinier-fichiste-delete-dialog.component';
import { MangasinierFichisteRoutingModule } from './route/mangasinier-fichiste-routing.module';
import { SidebarModule } from 'app/layouts/sidebar/sidebar.module';

@NgModule({
  imports: [SharedModule, MangasinierFichisteRoutingModule, SidebarModule],
  declarations: [
    MangasinierFichisteComponent,
    MangasinierFichisteDetailComponent,
    MangasinierFichisteUpdateComponent,
    MangasinierFichisteDeleteDialogComponent,
  ],
  entryComponents: [MangasinierFichisteDeleteDialogComponent],
})
export class MangasinierFichisteModule {}
