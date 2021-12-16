package academy.devdojo.springboot2.requests.anime;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AnimePostRequestBody {
    @NotNull
    private String name;
}
