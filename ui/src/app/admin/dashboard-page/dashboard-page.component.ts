import { Component, OnInit } from '@angular/core';
import {AdminService} from "../../services/admin.service";
import {User} from "../../models/User";

@Component({
  selector: 'app-dashboard-page',
  templateUrl: './dashboard-page.component.html',
  styleUrls: ['./dashboard-page.component.css']
})
export class DashboardPageComponent implements OnInit {

  users: User[];
  isUserDataLoaded = false;
  displayedColumns : string[] = ['position', 'name', 'weight', 'symbol'];
  dataSource;

  constructor(
    private adminService: AdminService
  ) { }

  ngOnInit(): void {
    this.adminService.getAllUsers()
      .subscribe(data => {
        console.log(data);
        this.users = data;
        this.isUserDataLoaded = true;
        this.dataSource = data;
      });
  }



}
