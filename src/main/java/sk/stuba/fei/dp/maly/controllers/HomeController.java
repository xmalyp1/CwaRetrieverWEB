package sk.stuba.fei.dp.maly.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Patrik on 27/12/2016.
 */
@Controller
@RequestMapping
public class HomeController {

    @RequestMapping(value = "",method = RequestMethod.GET)
    public String home(Model model) {
        return "index";
    }
}
