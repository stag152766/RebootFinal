import {Component, OnInit} from '@angular/core';
import {Post} from "../../models/Post";
import {PostService} from "../../services/post.service";
import {CommentService} from "../../services/comment.service";
import {ImageService} from "../../services/image.service";
import {NotificationService} from "../../services/notification.service";

@Component({
  selector: 'app-favorite-posts',
  templateUrl: './favorite-posts.component.html',
  styleUrls: ['./favorite-posts.component.css']
})
export class FavoritePostsComponent implements OnInit {

  isUserPostsLoaded = false;
  posts: Post[];

  constructor(private postService: PostService,
              private imageService: ImageService,
              private commentService: CommentService,
              private notificationService: NotificationService) {
  }

  ngOnInit(): void {
    this.postService.getFavoritePostForUser()
      .subscribe(data => {
        console.log(data);
        this.posts = data;
        this.getImagesToPost(this.posts);
        this.getCommentsToPost(this.posts);
        this.isUserPostsLoaded = true;
      });
  }

  private getImagesToPost(posts: Post[]): void {
    posts.forEach(p => {
      this.imageService.getImageToPost(p.id)
        .subscribe(data => {
          p.image = data.imageBytes;
        });
    });
  }

  private getCommentsToPost(posts: Post[]): void {
    posts.forEach(p => {
      this.commentService.getCommentsToPost(p.id)
        .subscribe(data => {
          p.comments = data;
        });
    });
  }

  removePost(post: Post, index: number): void {
    console.log(post);
    const result = confirm('Do you really want to delete this post?');
    if (result) {
      this.postService.delete(post.id)
        .subscribe(() => {
          this.posts.splice(index, 1);
          this.notificationService.showSnackBar('Post deleted');
        });
    }

  }
  formatImage(img: any): any {
    if (img == null) {
      return null;
    }
    return 'data:image/jpeg;base64,' + img;
  }

}
