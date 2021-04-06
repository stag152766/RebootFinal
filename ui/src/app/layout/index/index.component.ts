import {Component, OnInit} from '@angular/core';
import {Post} from "../../models/Post";
import {User} from "../../models/User";
import {PostService} from "../../services/post.service";
import {UserService} from "../../services/user.service";
import {CommentService} from "../../services/comment.service";
import {NotificationService} from "../../services/notification.service";
import {ImageService} from "../../services/image.service";
import {Category} from "../../models/Category";
import {NavigationComponent} from "../navigation/navigation.component";
import {CategoryService} from "../../services/category.service";

@Component({
  selector: 'app-index',
  templateUrl: './index.component.html',
  styleUrls: ['./index.component.css']
})
export class IndexComponent implements OnInit {

  isPostsLoaded = false;
  posts: Post[];
  user: User;
  isUserDataLoaded = false;
  categories: Category[];
  categoryGroupSelected: Category;



  constructor(
    private postService: PostService,
    private userService: UserService,
    private commentService: CommentService,
    private notificationService: NotificationService,
    private imageService: ImageService,
    private categoryService: CategoryService
  ) {
  }

  ngOnInit(): void {
    this.postService.getAllPosts()
      .subscribe(data => {
        console.log(data);
        this.posts = data;
        this.getImagesToPosts(this.posts);
        this.getCommentsToPosts(this.posts);
        this.isPostsLoaded = true;
      });

    this.userService.getCurrentUser()
      .subscribe(data => {
        console.log(data);
        this.user = data;
        this.isUserDataLoaded = true;
      });

    this.categoryService.getCategories()
      .subscribe(data => {
        this.categories = data;
          console.log(data);

        }
      )
  }


  getImagesToPosts(posts: Post[]): void {
    posts.forEach(p => {
      this.imageService.getImageToPost(p.id)
        .subscribe(data => {
          p.image = data.imageBytes;
        });
    });
  }

  getCommentsToPosts(posts: Post[]): void {
    posts.forEach(p => {
      this.commentService.getCommentsToPost(p.id)
        .subscribe(data => {
          p.comments = data;
        });
    });
  }

  favoritePost(postId: number, postIndex: number): void {
    const post = this.posts[postIndex];
    console.log(post);


    if (!post.usersFavorited.includes(this.user.username)) {
      this.postService.favoritePost(postId, this.user.username)
        .subscribe(() => {
          post.usersFavorited.push(this.user.username);
          this.notificationService.showSnackBar('Added to favorite!');

        });
    } else {
      this.postService.favoritePost(postId, this.user.username)
        .subscribe(() => {
          const index = post.usersFavorited.indexOf(this.user.username, 0);
          if (index > -1) {
            post.usersFavorited.splice(index, 1);
          }
        });
    }
  }

  postComment(message: string, postId: number, postIndex: number): void {
    const post = this.posts[postIndex];

    console.log(post);
    this.commentService.addToCommentToPost(postId, message)
      .subscribe(data => {
        console.log(data);
        post.comments.push(data);
      });
  }

  formatImage(img: any): any {
    if (img == null) {
      return null;
    }
    return 'data:image/jpeg;base64,' + img;
  }




}
