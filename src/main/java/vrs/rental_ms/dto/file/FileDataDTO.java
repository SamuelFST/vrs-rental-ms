package vrs.rental_ms.dto.file;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.http.MediaType;

import java.io.InputStream;

@Setter
@Getter
@Accessors(chain = true)
public class FileDataDTO {

    private InputStream inputStream;
    private String filename;
    private Long fileSize;
    private MediaType contentType;

}
