import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {RouterModule} from "@angular/router";
import {AdminLayoutComponent} from './components/admin-layout/admin-layout.component';
import {LoginPageComponent} from './login-page/login-page.component';
import {DashboardPageComponent} from './dashboard-page/dashboard-page.component';
import {AuthAdminGuardService} from "../services/auth-admin-guard.service";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MaterialModule} from "../material-module";
import {MatTableModule} from "@angular/material/table";


@NgModule({
  declarations: [
    AdminLayoutComponent,
    LoginPageComponent,
    DashboardPageComponent
  ],
  imports: [
    CommonModule,
    RouterModule.forChild([
      {
        path: '', component: AdminLayoutComponent, children: [
          {path: '', redirectTo: '/admin/login', pathMatch: 'full'},
          {path: 'login', component: LoginPageComponent},
          {
            path: 'dashboard', component: DashboardPageComponent,
            canActivate: [AuthAdminGuardService], data: {roles: ['ROLE_ADMIN']}
          }
        ]
      }
    ]),
    MaterialModule,
    ReactiveFormsModule,
    FormsModule,
    MatTableModule


  ],
  exports: [RouterModule]
})

export class AdminModule {

}
