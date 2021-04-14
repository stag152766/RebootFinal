import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Post} from "../models/Post";
import {Observable} from "rxjs";


const POST_API = 'http://localhost:8080/api/post/';

@Injectable({
  providedIn: 'root'
})
export class PostService {

  constructor(private http: HttpClient) {
  }

  createPost(post: Post): Observable<any> {
    return this.http.post(POST_API + 'create', post);
  }

  getAllPosts(): Observable<any> {
    return this.http.get(POST_API + 'all');
  }

  getPostForCurrentUser(): Observable<any> {
    return this.http.get(POST_API + 'user/posts');
  }

  getFavoritePostForUser(): Observable<any> {
    return this.http.get(POST_API + 'user/favorites');
  }

  delete(id: number): Observable<any> {
    return this.http.post(POST_API + id + '/delete', null);
  }

  favoritePost(id: number, username: string): Observable<any> {
    return this.http.post(POST_API + id + '/' + username + '/favorite', null);
  }


}
