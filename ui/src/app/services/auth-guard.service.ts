import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, Router, RouterStateSnapshot, UrlTree} from "@angular/router";
import {TokenStorageService} from "./token-storage.service";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AuthGuardService {


  constructor(private router: Router,
              private tokenService: TokenStorageService) {
  }

  // проверка является ли пользователь авторизованным
  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    // содержатся ли данные в браузере
    const currentUser = this.tokenService.getUser();
    if (currentUser) {
      return true;
    }

    // если нет, то редирект на логин
    this.router.navigate(['/login'], {queryParams: {returnUrl: state.url}});
    return false;
  }


}
