import {Role} from './Role';
export interface User {
  id: number;
  email: string;
  username: string;
  firstname: string;
  lastname: string;
  bio: string;
  roles: Role [];
  postCount: number;
  totalAmount: number;

}
