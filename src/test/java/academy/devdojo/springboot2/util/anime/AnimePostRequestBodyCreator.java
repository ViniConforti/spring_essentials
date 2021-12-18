package academy.devdojo.springboot2.util.anime;

import academy.devdojo.springboot2.requests.anime.AnimePostRequestBody;

public class AnimePostRequestBodyCreator {

    public static AnimePostRequestBody animePostRequestBody(){
        return AnimePostRequestBody.builder()
                .name(AnimeCreator.createAnimeToBeSaved().getName())
                .build();
    }
}
