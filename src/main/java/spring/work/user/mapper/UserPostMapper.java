package spring.work.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import spring.work.post.dto.request.CreatePost;
import spring.work.post.dto.request.UpdatePost;

@Mapper
public interface UserPostMapper {
    void saveUserPost(CreatePost post);
    void updateUserPost(@Param("postId") Long postId, @Param("post") UpdatePost post);
}