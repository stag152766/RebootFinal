import {Comment} from './Comment';
import {Category} from "./Category";

export interface Post {
  id?: number;
  title: string;
  caption: string;
  location: string;
  image?: File;
  usersFavorited?: string[];
  comments?: Comment [];
  username?: string;
  category: Category;
}
