package academy.devdojo.springboot2.controller;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.service.AnimeService;
import academy.devdojo.springboot2.util.AnimeCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DisplayName("Tests for Anime controller")
class AnimesControllerTest {

    //Utilizado quando quero testar a classe em si
    @InjectMocks
    private AnimesController animesController;


    //Utilizado para fazer um mock dos objetos utilizados dentro da classe que estou testando
    @Mock
    private AnimeService animeServiceMock;

    @BeforeEach
    void setup(){
        PageImpl<Anime> animePage =  new PageImpl<>(List.of(AnimeCreator.createValidAnime()));
        BDDMockito.when(animeServiceMock.listAll(ArgumentMatchers.any()))
                .thenReturn(animePage);
    }

    @Test
    @DisplayName("Returns a list of animes inside a page object when successful")
        //Padrao nomenclatura:Método_O que ele deve fazer_quando
    void list_returnsListOfAnimesInsidePageObject_whenSuccessful(){
        Anime createdAnime = AnimeCreator.createValidAnime();

        Page<Anime> animePage = this.animesController.list(null).getBody();

        Assertions.assertThat(animePage.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animePage.toList().get(0)).isEqualTo(createdAnime);
    }

}