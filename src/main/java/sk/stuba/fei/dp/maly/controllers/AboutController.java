package sk.stuba.fei.dp.maly.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import sk.stuba.fei.dp.maly.configuration.ApplicationProperties;

/**
 * Created by Patrik on 04/04/2017.
 */
@Controller
@RequestMapping(path = "/about")
public class AboutController {

    private ApplicationProperties applicationProperties;

    @Autowired
    public AboutController(ApplicationProperties applicationProperties){
        this.applicationProperties = applicationProperties;
    }

    @RequestMapping(value = "",method = RequestMethod.GET)
    public String ontology(Model model) {
        model.addAttribute("api",applicationProperties.getInstanceRetrieverApiVersion());
        return "about";
    }


}
