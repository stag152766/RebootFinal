import { Component, OnInit } from '@angular/core';
import {User} from "../../models/User";
import {TokenStorageService} from "../../services/token-storage.service";
import {PostService} from "../../services/post.service";
import {MatDialog, MatDialogConfig} from "@angular/material/dialog";
import {NotificationService} from "../../services/notification.service";
import {UserService} from "../../services/user.service";
import {ImageService} from "../../services/image.service";
import {EditUserComponent} from "../edit-user/edit-user.component";


@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  // флаг для загрузки данных
  // начинает рендеринг страницы когда true
  isUserDataLoaded = false;
  user: User;
  selectedFile: File;
  userProfileImage: File;
  // превью аватарки, которую выбрал юзер
  previewImgURL: any;

  constructor(private tokenService: TokenStorageService,
              private postService: PostService,
              private dialog: MatDialog,
              private notificationService: NotificationService,
              private imageService: ImageService,
              private userService: UserService) {
  }

  ngOnInit(): void {
    // загрузка юзера
    this.userService.getCurrentUser()
      .subscribe(data => {
        this.user = data;
        this.isUserDataLoaded = true;
      });

    // загрузка аватарки
    this.imageService.getProfileImage()
      .subscribe(data => {
        this.userProfileImage = data.imageBytes;
      });
  }

  // выбор файла для загрузки
  onFileSelected(event): void {
    this.selectedFile = event.target.files[0];

    const reader = new FileReader();
    reader.readAsDataURL(this.selectedFile);
    // показать превью перед загрузкой на сервер
    reader.onload = () => {
      this.previewImgURL = reader.result;
    };
  }

  // диалог для изменения данных юзера
  openEditDialog(): void {
    // конфигурация диалога
    const dialogUserEditConfig = new MatDialogConfig();
    dialogUserEditConfig.width = '400px';
    dialogUserEditConfig.data = {
      user: this.user
    };
    this.dialog.open(EditUserComponent, dialogUserEditConfig);
  }

  // фото приходит в байтах
  // метод трансформации байтов в изображение
  formatImage(img: any): any {
    if (img == null) {
      return null;
    }
    return 'data:image/jpeg;base64,' + img;
  }

  // загрузка фото
  onUpload(): void {
    if (this.selectedFile != null) {
      this.imageService.uploadImageToUser(this.selectedFile)
        .subscribe(() => {
          this.notificationService.showSnackBar('Profile image uploaded successfully');
        });
    }

  }

  // список избранных постов
  openFavoritesPost(): void {

  }
}

