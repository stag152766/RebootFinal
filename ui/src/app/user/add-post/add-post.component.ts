import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {NotificationService} from "../../services/notification.service";
import {Post} from "../../models/Post";
import {PostService} from "../../services/post.service";
import {Router} from "@angular/router";
import {ImageService} from "../../services/image.service";
import {Category} from "../../models/Category";
import {CategoryService} from "../../services/category.service";
import {isElementClippedByScrolling} from "@angular/cdk/overlay/position/scroll-clip";


@Component({
  selector: 'app-add-post',
  templateUrl: './add-post.component.html',
  styleUrls: ['./add-post.component.css']
})
export class AddPostComponent implements OnInit {

  postForm: FormGroup;
  selectedFile: File;
  isPostCreated = false;
  createdPost: Post;
  previewImgURL: any;
  categories: Category[];
  selectedCategory: Category;

  constructor(private postService: PostService,
              private imageService: ImageService,
              private categoryService: CategoryService,
              private notificationService: NotificationService,
              private router: Router,
              private fb: FormBuilder) {
  }

  ngOnInit(): void {
    this.postForm = this.createPostForm();
  }

  /**
   * Форма для создания поста
   * @private
   */
  private createPostForm(): FormGroup {
    return this.fb.group({
      title: ['', Validators.compose([Validators.required])],
      caption: ['', Validators.compose([Validators.required])],
      location: ['', Validators.compose([Validators.required])],
      categories: this.categoryService.getCategories()
        .subscribe(data => {
            console.log(data);
            this.categories = data;
          }
        )
    })
      ;
  }

  /**
   * Метод для отправки поста на сервер
   */
  submit(): void {
    console.log(this.selectedCategory)
    this.postService.createPost({
      title: this.postForm.value.title,
      caption: this.postForm.value.caption,
      location: this.postForm.value.location,
      category: this.selectedCategory
    }).subscribe(data => {
      this.createdPost = data; // создали пост и получили обратно объект из бд
      console.log('Post created');
      console.log('PostDTO ',data);

      const formData = {
        title: this.postForm.value.title,
          caption: this.postForm.value.caption,
          location: this.postForm.value.location,
          category: this.selectedCategory}
      console.log('Form data ', formData)



      // если получили ид с сервера, значит пост создан
      if (this.createdPost.id != null) {
        // загружаем фото по ид поста
        this.imageService.uploadImageToPost(this.selectedFile, this.createdPost.id)
          .subscribe(() => {
            this.notificationService.showSnackBar('Post created successfully');
            this.isPostCreated = true;
            this.router.navigate(['profile']);
          });
      }
    });
  }


  onFileSelected(event): void {
    this.selectedFile = event.target.files[0];

    const reader = new FileReader();
    reader.readAsDataURL(this.selectedFile);
    reader.onload = (e) => {
      this.previewImgURL = reader.result;
    };
  }

}
