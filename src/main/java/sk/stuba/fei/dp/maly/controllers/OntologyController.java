package sk.stuba.fei.dp.maly.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sk.stuba.fei.dp.maly.configuration.ApplicationProperties;
import sk.stuba.fei.dp.maly.configuration.WebMvcConfiguration;
import sk.stuba.fei.dp.maly.persistence.dto.OntologyDto;
import sk.stuba.fei.dp.maly.persistence.entities.Ontology;
import sk.stuba.fei.dp.maly.persistence.enums.OWLFileTypeEnum;
import sk.stuba.fei.dp.maly.services.OntologyService;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Patrik on 28/12/2016.
 */
@Controller
@RequestMapping(path = "/ontology")
public class OntologyController {


    private OntologyService ontologyService;
    private ApplicationProperties applicationProperties;

    @Autowired
    public OntologyController(OntologyService service,ApplicationProperties props){
        ontologyService = service;
        applicationProperties = props;
    }


    @RequestMapping(value = "",method = RequestMethod.GET)
    public String ontology(Model model) {
        model.addAttribute("ontologies",ontologyService.findAll());
        return "ontology";
    }

    @RequestMapping(value = "",method = RequestMethod.DELETE)
    public ResponseEntity ontology(@RequestParam("oid") long ontologyId ) {
        Ontology o = ontologyService.findById(ontologyId);
        if(o == null)
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        File f = new File(o.getDestinationFile());
        if(f!=null && f.exists()){
            if(!f.delete())
                return new ResponseEntity(HttpStatus.FORBIDDEN);

        }else{
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        ontologyService.delete(o);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public String ontology(@ModelAttribute("ontology") OntologyDto ontology, BindingResult bindingResult, @RequestParam("file") MultipartFile file) {

        if(bindingResult.hasErrors()){
            for(ObjectError e: bindingResult.getAllErrors()){
                System.out.println(e.toString());
                //TODO : Display an error and handle the exception
            }
        }

        File fileForTransfer = null;
        File root = new File(applicationProperties.getFileUploadRootFolder());
        if(!root.exists())
            root.mkdirs();

        try {
            fileForTransfer = new File(applicationProperties.buildFilePath(file.getOriginalFilename()));
            file.transferTo(fileForTransfer);
        } catch (IOException e) {
            //Todo: handle exception
        }

        Ontology ont = new Ontology();
        ont.setName(ontology.getName());
        ont.setDescription(ontology.getDescription());
        ont.setFileType(ontology.getFileType());
        ont.setDestinationFile(fileForTransfer == null ? "" : fileForTransfer.getAbsolutePath());

        ontologyService.create(ont);

        return "redirect:/ontology";
    }

    @RequestMapping(value = "/add",method = RequestMethod.GET)
    public String createOntology(Model model) {
        model.addAttribute("ontology",new OntologyDto());
        return "add_ontology";
    }
    @RequestMapping(value = "/{ontologyId}",method = RequestMethod.GET)
    public void downloadOntology(@PathVariable long ontologyId,HttpServletRequest request,
                                 HttpServletResponse response){

        try {
            ontologyService.downloadOntology(ontologyService.findById(ontologyId),request,response);
        } catch (IOException e) {
            e.printStackTrace();
            request.setAttribute("error",1);
        }
    }

    @ModelAttribute(name = "owlFileTypes")
    public List<OWLFileTypeEnum> getOntologyFileTypes(){
        return Arrays.asList(OWLFileTypeEnum.values());
    }


}
