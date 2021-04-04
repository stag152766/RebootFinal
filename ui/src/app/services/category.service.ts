import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Category} from "../models/Category";

const CATEGORY_API = 'http://localhost:8080/api/category/'

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  selectedCategory: Category;

  constructor(private http: HttpClient) {
  }

  getCategories(): Observable<any> {
    return this.http.get(CATEGORY_API);
  }
}
