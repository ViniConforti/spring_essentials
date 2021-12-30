package academy.devdojo.springboot2.service;

import academy.devdojo.springboot2.domain.DevDojoUser;
import academy.devdojo.springboot2.repository.DevDojoUserRepository;
import academy.devdojo.springboot2.util.user.UserCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
@DisplayName("Tests for Dev Dojo User Details Service")
class DevDojoUserDetailsServiceTest {

    //Utilizado quando quero testar a classe em si
    @InjectMocks
    private DevDojoUserDetailsService devDojoUserDetailsService;

    //Utilizado para fazer um mock dos objetos utilizados dentro da classe que estou testando
    @Mock
    private DevDojoUserRepository devDojoUserRepositoryMock;

    @BeforeEach
    void setup(){
        BDDMockito.when(devDojoUserRepositoryMock.findByUsername(ArgumentMatchers.anyString()))
                        .thenReturn(UserCreator.userAdminValid());
    }

    @Test
    @DisplayName("Find user by name return user details when successful")
        //Padrao nomenclatura:Método_O que ele deve fazer_quando
    void findUserByName_returnsUserDetails_whenSuccessful(){
        DevDojoUser validUser = UserCreator.userAdminValid();
        UserDetails userDetails = this.devDojoUserDetailsService.loadUserByUsername(validUser.getUsername());

        Assertions.assertThat(userDetails).isNotNull();
        Assertions.assertThat(userDetails.getUsername()).isEqualTo(validUser.getUsername());
        Assertions.assertThat(userDetails.getAuthorities()).isEqualTo(validUser.getAuthorities());
        Assertions.assertThat(userDetails.getPassword()).isEqualTo(validUser.getPassword());

    }

    @Test
    @DisplayName("Throws username not found exception when user is not found")
        //Padrao nomenclatura:Método_O que ele deve fazer_quando
    void findUserByName_throwsUserNameNotFoundException_whenUserNotFound(){

        BDDMockito.when(this.devDojoUserRepositoryMock.findByUsername(ArgumentMatchers.anyString()))
                        .thenReturn(null);

        Assertions.assertThatThrownBy(()->
                        this.devDojoUserDetailsService.loadUserByUsername("nao achei"))
                .isInstanceOf(UsernameNotFoundException.class);

    }


}