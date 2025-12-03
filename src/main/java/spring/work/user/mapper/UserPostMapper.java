package spring.work.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import spring.work.user.dto.request.CreatePost;

@Mapper
public interface UserPostMapper {
    void saveUserPost(CreatePost post);
}
