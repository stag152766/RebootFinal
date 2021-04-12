import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Router, RouterStateSnapshot, UrlTree} from "@angular/router";
import {TokenStorageService} from "./token-storage.service";
import {Observable} from "rxjs";
import {UserService} from "./user.service";
import {User} from "../models/User";



@Injectable({
  providedIn: 'root'
})
export class AuthAdminGuardService {

  user: User;


  constructor(private router: Router,
              private tokenService: TokenStorageService,
              private userService: UserService
  ) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot):
    Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    const currentUser = this.tokenService.getUser();

    // содержатся ли данные в браузере
    console.log('currentUser: ',currentUser)
    if (currentUser) {
        // TODO если админские права есть, то проверка прошла
        return true;
      }
      // если нет админских прав, то редирект на страницу логина юзера
      this.router.navigate(['/login'])
      return false;


    // если не авторизован, то редирект на логин
    this.router.navigate(['admin/login'], {queryParams: {returnUrl: state.url}});
    return false;
  }
}
