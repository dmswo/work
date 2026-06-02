package spring.work.global.security.config;

public final class SecurityUrls {
    public static final String[] AUTH_URLS = {
            "/user/signup",
            "/user/login"
    };

    public static final String[] SWAGGER_URLS = {
            "/swagger",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/api-docs",
            "/api-docs/**",
            "/v3/api-docs/**"
    };

    public static final String[] PUBLIC_GET_URLS = {
            "/post",
            "/posts/*/comments",
            "/posts/*/comments/*/replies"
    };

    private SecurityUrls() {
    }
}
