package academy.devdojo.springboot2.util.anime;

import academy.devdojo.springboot2.domain.Anime;

public class AnimeCreator {
    public static Anime createAnimeToBeSaved(){
        return Anime.builder().name("Teste").build();
    }

    public static Anime createValidAnime(){
        return Anime.builder()
                .name(createAnimeToBeSaved().getName())
                .id(11L)
                .build();
    }

    public static Anime createValidUpdateAnime(){
        return Anime.builder()
                .name("UpdatedAnime")
                .id(createValidAnime().getId())
                .build();
    }

}
