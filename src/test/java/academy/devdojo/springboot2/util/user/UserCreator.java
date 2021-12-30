package academy.devdojo.springboot2.util.user;

import academy.devdojo.springboot2.domain.DevDojoUser;

public class UserCreator {
    public static DevDojoUser userCommon(){
        return DevDojoUser.builder()
                .name("test_common_user")
                .username("test")
                .password("{bcrypt}$2a$10$7Y/2aAZhGrbIhokF1jElAeJm8902scaOv4.X/BCcsRwObEUh3GY8a")
                .authorities("ROLE_USER").build();
    }

    public static DevDojoUser userAdmin(){
        return DevDojoUser.builder()
                .name("test_admin_user")
                .username("test_admin")
                .password("{bcrypt}$2a$10$7Y/2aAZhGrbIhokF1jElAeJm8902scaOv4.X/BCcsRwObEUh3GY8a")
                .authorities("ROLE_USER,ROLE_ADMIN").build();
    }

    public static DevDojoUser userAdminValid(){
        return DevDojoUser.builder()
                .id(1L)
                .name("test_admin_user")
                .username("test_admin")
                .password("{bcrypt}$2a$10$7Y/2aAZhGrbIhokF1jElAeJm8902scaOv4.X/BCcsRwObEUh3GY8a")
                .authorities("ROLE_USER,ROLE_ADMIN").build();
    }
}
