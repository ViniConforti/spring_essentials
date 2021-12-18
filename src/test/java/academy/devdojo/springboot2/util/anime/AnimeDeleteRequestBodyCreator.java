package academy.devdojo.springboot2.util.anime;

import academy.devdojo.springboot2.requests.anime.AnimeDeleteRequestBody;

public class AnimeDeleteRequestBodyCreator {

    public static AnimeDeleteRequestBody animeDeleteRequestBody(){
        return AnimeDeleteRequestBody.builder()
                .id(AnimeCreator.createValidAnime().getId())
                .build();
    }
}
