package spring.work.user.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.work.global.entity.BaseEntity;
import spring.work.user.constant.UserRole;
import spring.work.user.dto.request.Signup;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Users extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(length = 50, nullable = false, unique = true)
    private String userId;

    @Column(length = 200, nullable = false)
    private String password;

    @Column(length = 200)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Column(length = 200)
    private String email;

    @Column(length = 200)
    private String phone;

    @Column(length = 200)
    private String address;

    public static Users create(Signup dto) {
        return Users.builder()
                .userId(dto.getUserId())
                .password(dto.getPassword())
                .nickname(dto.getNickname())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .userRole(UserRole.ROLE_USER)
                .build();
    }
}
