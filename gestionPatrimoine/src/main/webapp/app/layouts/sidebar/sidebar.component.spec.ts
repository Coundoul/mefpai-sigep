jest.mock('@angular/router');
jest.mock('ngx-webstorage');
jest.mock('@ngx-translate/core');
jest.mock('app/core/auth/account.service');
jest.mock('app/login/login.service');

import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { SessionStorageService } from 'ngx-webstorage';
import { TranslateService } from '@ngx-translate/core';

import { ProfileInfo } from 'app/layouts/profiles/profile-info.model';
import { AccountService } from 'app/core/auth/account.service';
import { ProfileService } from 'app/layouts/profiles/profile.service';
import { LoginService } from 'app/login/login.service';
import { SidebarComponent } from './sidebar.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

describe('Component Tests', () => {
  describe('Sidebar Component', () => {
    let comp: SidebarComponent;
    let fixture: ComponentFixture<SidebarComponent>;
    let mockAccountService: AccountService;
    let profileService: ProfileService;

    beforeEach(
      waitForAsync(() => {
        TestBed.configureTestingModule({
          declarations: [SidebarComponent],
          imports: [BrowserAnimationsModule],
        }).compileComponents();
      })
    );

    beforeEach(
      waitForAsync(() => {
        TestBed.configureTestingModule({
          imports: [HttpClientTestingModule],
          declarations: [SidebarComponent],
          providers: [SessionStorageService, TranslateService, Router, LoginService],
        })
          .overrideTemplate(SidebarComponent, '')
          .compileComponents();
      })
    );

    beforeEach(() => {
      fixture = TestBed.createComponent(SidebarComponent);
      comp = fixture.componentInstance;
      mockAccountService = TestBed.inject(AccountService);
      profileService = TestBed.inject(ProfileService);
    });

    it('Should call profileService.getProfileInfo on init', () => {
      // GIVEN
      spyOn(profileService, 'getProfileInfo').and.returnValue(of(new ProfileInfo()));

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(profileService.getProfileInfo).toHaveBeenCalled();
    });
  });
});
