package academy.devdojo.springboot2.requests.anime;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class AnimePostRequestBody {
    @NotNull
    private String name;
}
