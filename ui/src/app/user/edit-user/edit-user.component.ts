import {Component, Inject, OnInit} from '@angular/core';
import {User} from "../../models/User";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {UserService} from "../../services/user.service";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {NotificationService} from "../../services/notification.service";

@Component({
  selector: 'app-edit-user',
  templateUrl: './edit-user.component.html',
  styleUrls: ['./edit-user.component.css']
})
export class EditUserComponent implements OnInit {

  public profileEditForm: FormGroup;


  constructor(private dialogRef: MatDialogRef<EditUserComponent>,
              private fb: FormBuilder,
              private notificationService: NotificationService,
              @Inject(MAT_DIALOG_DATA) public data,
              private userService: UserService) {
  }

  ngOnInit(): void {
    this.profileEditForm = this.createProfileForm();
  }

  createProfileForm(): FormGroup {
    // когда пользователь открывает диалог, то в полях заполнены текущие данные
    return this.fb.group({
      firstName: [
        this.data.user.firstname,
        Validators.compose([Validators.required])
      ],
      lastName: [
        this.data.user.lastname,
        Validators.compose([Validators.required])
      ],
      bio: [
        this.data.user.bio,
        Validators.compose([Validators.required])
      ]
    });
  }


  submit(): void {
    this.userService.updateUser(this.updateUser())
      .subscribe(() => {
        this.notificationService.showSnackBar('User updated successfully');
        this.dialogRef.close();
      });
  }

  // Вспомогательный метод для обновления данных юзера
  // данные вводятся в форму, приходят в компоненту, отправляются на сервер
  private updateUser(): User {
    this.data.user.firstname = this.profileEditForm.value.firstName;
    this.data.user.lastname = this.profileEditForm.value.lastName;
    this.data.user.bio = this.profileEditForm.value.bio;
    return this.data.user;
  }

  // Закрыть окно без изменений
  closeDialog(): void {
    this.dialogRef.close();
  }
}
