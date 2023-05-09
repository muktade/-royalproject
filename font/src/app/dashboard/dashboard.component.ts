import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from '../service/api.service';
import { Employee } from '../service/Employee';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
})
export class DashboardComponent {
  email: any;
  employee: any;

  time: any;
  lastLoginTime: any;
  constructor(private api: ApiService, private router: Router) {
    this.time = this.timeStampToDate(new Date().getTime());
  }

  ngOnInit(): void {
    this.lastLoginTime = localStorage.getItem('time');
    if (this.lastLoginTime != 'null') {
      this.lastLoginTime = this.timeStampToDate(this.lastLoginTime);
    } else {
      this.lastLoginTime = 'New User.';
    }
    this.email = localStorage.getItem('email');
    if (this.email != null) {
      this.getEmployeeDetails();
      this.router.navigate(['/dashboard']);
    } else {
      this.router.navigate(['/login']);
    }
  }
  timeStampToDate(lastLoginTime: any): any {
    debugger;
    var theDate = new Date(lastLoginTime * 1)
    let dateString = theDate.toDateString();
    return dateString;
  }

  getEmployeeDetails() {
    this.api.getRequest(`find-employee/${this.email}`).subscribe((res: any) => {
      debugger;
      this.employee = res[0];
    });
  }

  empLogout() {
    localStorage.clear();
    this.router.navigate(['/login']);
  }
}
