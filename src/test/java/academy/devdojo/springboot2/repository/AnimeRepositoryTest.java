package academy.devdojo.springboot2.repository;

import academy.devdojo.springboot2.domain.Anime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@DisplayName("Tests for Anime Repository")
class AnimeRepositoryTest {
    @Autowired
    private AnimeRepository animeRepository;

    @Test
    @DisplayName("Save creates anime when successful")
    //Padrao nomenclatura:Método_O que ele deve fazer_quando
    void save_persistAnime_whenSuccessful(){
        Anime animeGenerated = this.generateAnime();
        Anime savedAnime = this.animeRepository.save(animeGenerated);
        Assertions.assertThat(savedAnime).isNotNull();
        Assertions.assertThat(savedAnime.getId()).isNotNull();
        Assertions.assertThat(savedAnime.getName()).isEqualTo(animeGenerated.getName());
    }

    public Anime generateAnime(){
        return Anime.builder().name("Teste").build();
    }
}