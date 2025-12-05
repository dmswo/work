package spring.work.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import spring.work.user.dto.request.post.CreatePost;
import spring.work.user.dto.request.post.UpdatePost;

@Mapper
public interface UserPostMapper {
    void saveUserPost(CreatePost post);
    void updateUserPost(@Param("postId") Long postId, @Param("post") UpdatePost post);
}