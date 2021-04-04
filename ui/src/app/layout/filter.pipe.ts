import {Pipe, PipeTransform} from "@angular/core";
import {Post} from "../models/Post";
import {Category} from "../models/Category";


@Pipe(
  {name:'filterPosts'}
)
export class FilterPipe implements PipeTransform{
  transform(posts: Post[], selectedCategoryGroup: Category): Post[]  {
    if (selectedCategoryGroup === undefined){
      return posts;
    }
    return posts.filter(post=>post.category.id === selectedCategoryGroup.id)
  }
}
