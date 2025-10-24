package spring.work.user.service;

import spring.work.user.dto.response.UserPointResponse;

public interface UserPointService {
    UserPointResponse getUserPoint(String userId);
}
