package imageboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
}
