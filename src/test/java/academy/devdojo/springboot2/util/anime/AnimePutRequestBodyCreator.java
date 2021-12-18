package academy.devdojo.springboot2.util.anime;
import academy.devdojo.springboot2.requests.anime.AnimePutRequestBody;

public class AnimePutRequestBodyCreator {
    public static AnimePutRequestBody animePutRequestBody(){
        return AnimePutRequestBody.builder()
                .name(AnimeCreator.createValidUpdateAnime().getName())
                .id(AnimeCreator.createValidUpdateAnime().getId())
                .build();
    }
}
