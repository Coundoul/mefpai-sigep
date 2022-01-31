import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMangasinierFichiste, MangasinierFichiste } from '../mangasinier-fichiste.model';
import { MangasinierFichisteService } from '../service/mangasinier-fichiste.service';

@Injectable({ providedIn: 'root' })
export class MangasinierFichisteRoutingResolveService implements Resolve<IMangasinierFichiste> {
  constructor(protected service: MangasinierFichisteService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMangasinierFichiste> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((mangasinierFichiste: HttpResponse<MangasinierFichiste>) => {
          if (mangasinierFichiste.body) {
            return of(mangasinierFichiste.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MangasinierFichiste());
  }
}
