import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { authGuard } from './modules/auth/auth-guard';
import { LoginFormComponent } from './modules/auth/login-form/login-form.component';
import { FrontPageComponent } from './front-page/front-page.component';
import { RegisterUserFormComponent } from './modules/auth/register-user-form/register-user-form.component';
import { RegisterPartnerFormComponent } from './modules/auth/register-partner-form/register-partner-form.component';
import { AdminDashboardComponent } from './modules/user/admin-dashboard/admin-dashboard.component';


const routes: Routes = [
  {path: '', component: FrontPageComponent, pathMatch: 'full'},
  {path: 'auth', component: LoginFormComponent},
  {path: 'register/user', component: RegisterUserFormComponent},
  {path: 'register/partner', component: RegisterPartnerFormComponent},
  {
    path: 'admin/dashboard',
    component: AdminDashboardComponent,
    canActivate: [authGuard]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
