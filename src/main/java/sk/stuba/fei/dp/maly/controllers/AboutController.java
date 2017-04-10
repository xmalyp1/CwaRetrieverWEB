package sk.stuba.fei.dp.maly.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import sk.stuba.fei.dp.maly.configuration.ApplicationProperties;
import sk.stuba.fei.dp.maly.services.AboutService;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Patrik on 04/04/2017.
 */
@Controller
@RequestMapping(path = "/about")
public class AboutController {

    private ApplicationProperties applicationProperties;
    private AboutService aboutService;

    @Autowired
    public AboutController(ApplicationProperties applicationProperties,AboutService aboutService){
        this.applicationProperties = applicationProperties;
        this.aboutService = aboutService;
    }

    @RequestMapping(value = "",method = RequestMethod.GET)
    public String ontology(Model model) {
        model.addAttribute("api",applicationProperties.getInstanceRetrieverApiVersion());
        return "about";
    }

    @RequestMapping(value="/api",method = RequestMethod.GET)
    public void downloadAPI(HttpServletRequest request,
                            HttpServletResponse response){

        try {
            aboutService.downloadAPI(applicationProperties.getFileDownload(),request,response);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
