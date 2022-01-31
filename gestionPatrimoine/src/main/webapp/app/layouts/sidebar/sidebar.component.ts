import { RendererFactory2 } from '@angular/core';
import { Renderer2 } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { ActivatedRouteSnapshot, NavigationEnd, NavigationError, Router } from '@angular/router';
import { LangChangeEvent, TranslateService } from '@ngx-translate/core';
import { LoginService } from 'app/login/login.service';
import * as dayjs from 'dayjs';

@Component({
  selector: 'jhi-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss'],
})
export class SidebarComponent implements OnInit {
  currentRout?: string;
  private renderer: Renderer2;
  private isNavbarCollapsed = true;
  private x: any;
  private y: any;
  private z: any;
  private a: any;
  private b: any;
  private c: any;
  private d: any;
  private e: any;
  private f: any;
  //valeurMatiere: any = 1;
  //valeurInfra:any = 1; valeurMaint:any = 1;

  constructor(
    private loginService: LoginService,
    private titleService: Title,
    private router: Router,
    private translateService: TranslateService,
    rootRenderer: RendererFactory2
  ) {
    this.renderer = rootRenderer.createRenderer(document.querySelector('html'), null);
    this.currentRout = this.router.url;
  }

  ngOnInit(): void {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.updateTitle();
      }
      if (event instanceof NavigationError && event.error.status === 404) {
        this.router.navigate(['/404']);
      }
    });

    this.translateService.onLangChange.subscribe((langChangeEvent: LangChangeEvent) => {
      this.updateTitle();
      dayjs.locale(langChangeEvent.lang);
      this.renderer.setAttribute(document.querySelector('html'), 'lang', langChangeEvent.lang);
    });
    // this.valeurMatiere;
    // this.valeurInfra;
    // this.valeurMaint;
  }

  getPageTitle(routeSnapshot: ActivatedRouteSnapshot): string {
    let title: string = routeSnapshot.data['pageTitle'] ?? '';
    if (routeSnapshot.firstChild) {
      title = this.getPageTitle(routeSnapshot.firstChild) || title;
    }
    return title;
  }

  updateTitle(): void {
    let pageTitle = this.getPageTitle(this.router.routerState.snapshot.root);
    if (!pageTitle) {
      pageTitle = 'global.title';
    }
    this.translateService.get(pageTitle).subscribe(title => this.titleService.setTitle(title));
  }

  logout(): void {
    this.loginService.logout();
    this.router.navigate(['']);
  }

  collapseNavbar(): void {
    this.isNavbarCollapsed = true;
  }

  toggleNavbar(): void {
    this.isNavbarCollapsed = !this.isNavbarCollapsed;
  }

  // cacherListeMatiere() : void{
  //   this.valeurMatiere=1;
  // }

  // afficherListeMatiere() : void{
  //   this.valeurMatiere=2;
  // }

  // cacherListeInfra() : void{

  // }

  // cacherListeMaint() : void{

  // }

  myFunction(): void {
     this.x = document.getElementById("adminMenuCache");
     this.y= document.getElementById("adminMenuAffiche");
     this.z= document.getElementById("adminMenuAfficheListe");
    if (this.y.style.display === "inline-block" && this.z.style.display === "inline-block" ) {
      this.y.style.display = "none";
      this.z.style.display = "none";
      this.x.style.display = "inline-block";
    } else {
      this.y.style.display = "inline-block";
      this.z.style.display = "inline-block";
      this.x.style.display = "none";
    }
  }

  myFunctionInfra(): void {
    this.a = document.getElementById("adminMenuCacheInfra");
    this.b= document.getElementById("adminMenuAfficheInfra");
    this.c= document.getElementById("adminMenuListeInfra");
   if (this.b.style.display === "inline-block" && this.c.style.display === "inline-block" ) {
     this.b.style.display = "none";
     this.c.style.display = "none";
     this.a.style.display = "inline-block";
   } else {
     this.b.style.display = "inline-block";
     this.c.style.display = "inline-block";
     this.a.style.display = "none";
   }
 }

 myFunctionMaint(): void {
  this.e = document.getElementById("adminMenuCacheMaint");
  this.f= document.getElementById("adminMenuAfficheMaint");
  this.d= document.getElementById("adminMenuListeMaint");
 if (this.d.style.display === "inline-block" && this.f.style.display === "inline-block" ) {
   this.d.style.display = "none";
   this.f.style.display = "none";
   this.e.style.display = "inline-block";
 } else {
   this.d.style.display = "inline-block";
   this.f.style.display = "inline-block";
   this.e.style.display = "none";
 }
}

}
