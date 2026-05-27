package spring.work.postlike.service;

public interface PostLikeService {
    void savePostLike(Long postId, String userId);
    void deletePostLike(Long postId, String userId);
}
