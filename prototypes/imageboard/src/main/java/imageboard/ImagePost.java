package imageboard;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.Assert;

import java.util.Base64;

/**
 * Created by spiollinux on 01.11.15.
 */

@Getter
@Setter
@Entity
public class ImagePost {

    @Id
    @GeneratedValue
    private long id;
    private String title;
    private String username;
    @Lob
    private byte[] picture;

    protected ImagePost() {};

    public ImagePost(String title, String username, byte[] picture) {
        Assert.hasText(title, "Title is required");
        Assert.hasText(username, "Username required");
        Assert.notNull(picture);
        this.title = title;
        this.picture = picture;
        this.username = username;
    }

    public String getBase64Picture() {
        String encodedPicture = Base64.getEncoder().encodeToString(picture);
        return encodedPicture;
    }

}
