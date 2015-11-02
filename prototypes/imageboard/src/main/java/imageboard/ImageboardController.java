package imageboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by spiollinux on 01.11.15.
 */
@Controller
class ImageboardController {

    private final Imageboard imageboard;

    @Autowired
    ImageboardController(Imageboard imageboard) {
        this.imageboard = imageboard;
    }

    @RequestMapping("/")
    String index() {
        return "redirect:/imageboard";
    }

    @RequestMapping(value = "/imageboard",method = RequestMethod.GET)
    public String showImages(Model model) {
        model.addAttribute("posts", imageboard.findAll());
        model.addAttribute("imagepost", new ImagePost());
        return "imageboard";
    }

    @RequestMapping(value = "/imageboard", method = RequestMethod.POST)
    public String addImagePost(Model model, @RequestParam("title") String title, @RequestParam("picture") MultipartFile picture) {
        if (!picture.isEmpty()) {
            try {
                byte[] pictureBytes = picture.getBytes();
                ImagePost post = new ImagePost(title,"anonymous",pictureBytes);
                imageboard.save(post);
            }
            //Pokemon exception
            catch (Exception e) {
               //TODO: error case
                System.out.println("error");
                return showImages(model);
            }
        }
        else {
            //TODO: empty picture
            System.out.println("empty");
            return showImages(model);
        }
        return "redirect:/imageboard";
    }
}
