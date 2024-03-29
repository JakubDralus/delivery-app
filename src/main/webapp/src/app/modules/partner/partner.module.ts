import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PartnerViewComponent } from './partner-view/partner-view.component';
import { CityViewComponent } from './city-view/city-view.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule } from '@angular/router';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { AppRoutingModule } from 'src/app/app-routing.module';
import { AdminPanelModule } from '../admin-panel/admin-panel.module';
import { AuthModule } from '../auth/auth.module';
import { UserModule } from '../user/user.module';
import { FilterPipe } from 'src/app/shared/filter.pipe';
import { PartnerEditFormComponent } from './partner-edit-form/partner-edit-form.component';
import { NavbarComponent } from 'src/app/shared/navbar/navbar.component';
import { NavbarModule } from 'src/app/shared/navbar/navbar.module';
import { FooterModule } from 'src/app/shared/footer/footer.module';

@NgModule({
  declarations: [
    PartnerViewComponent,
    CityViewComponent,
    FilterPipe,
    PartnerEditFormComponent,
  ],
  imports: [
    CommonModule,
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule,
    BrowserAnimationsModule,
    UserModule,
    AuthModule,
    AdminPanelModule,
    NavbarModule,
    FooterModule
  ],
  exports: [
    PartnerEditFormComponent
  ]
})
export class PartnerModule { }
