package imageboard;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by spiollinux on 01.11.15.
 */
public interface ImagePostForm {
    @NotBlank
    String getTitle();
    @NotBlank
    MultipartFile getPicture();
}
