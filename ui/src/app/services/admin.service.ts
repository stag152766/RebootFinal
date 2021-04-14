import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

const  ADMIN_API = 'http://localhost:8080/admin/';

@Injectable({
  providedIn: 'root'
})
export class AdminService{

  constructor(private http: HttpClient) {
  }

  getUserList(): Observable<any>{
    return this.http.get(ADMIN_API + 'dashboard');
  }


}
