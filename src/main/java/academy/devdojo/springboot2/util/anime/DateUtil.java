package academy.devdojo.springboot2.util.anime;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateUtil {
    public String FormatLocalDateTimeToDatabaseStyle (LocalDateTime datetime){
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss ").format(datetime);
    }
}
