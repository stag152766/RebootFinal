import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from "./auth/login/login.component";
import {RegisterComponent} from "./auth/register/register.component";
import {IndexComponent} from "./layout/index/index.component";
import {AuthGuardService} from "./services/auth-guard.service";
import {ProfileComponent} from "./user/profile/profile.component";
import {UserPostsComponent} from "./user/user-posts/user-posts.component";
import {AddPostComponent} from "./user/add-post/add-post.component";
import {FavoritePostsComponent} from "./user/favorite-posts/favorite-posts.component";

// Описание когда и где рендерить компоненты
const routes: Routes = [

  // иерархия компонентов
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'main', component: IndexComponent, canActivate: [AuthGuardService]}, // проверка авторизован ли юзер
  {
    path: 'profile', component: ProfileComponent, canActivate: [AuthGuardService],
    children: [
      {path: '', component: UserPostsComponent, canActivate: [AuthGuardService]},
      {path: 'add', component: AddPostComponent, canActivate: [AuthGuardService]},
      {path: 'favorites', component: FavoritePostsComponent, canActivate: [AuthGuardService]}
    ]
  },
  {path: '', redirectTo: 'main', pathMatch: 'full'} // если урл не существует (должен быть последним)

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
