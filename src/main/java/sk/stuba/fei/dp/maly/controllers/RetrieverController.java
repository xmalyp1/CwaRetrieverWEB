package sk.stuba.fei.dp.maly.controllers;

import org.dllearner.reasoning.ReasonerImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sk.stuba.fei.dp.maly.exceptions.RetrieverException;
import sk.stuba.fei.dp.maly.persistence.dto.OntologyDto;
import sk.stuba.fei.dp.maly.persistence.dto.RetrieverDataRequestDto;
import sk.stuba.fei.dp.maly.persistence.entities.Ontology;
import sk.stuba.fei.dp.maly.services.OntologyService;
import sk.stuba.fei.dp.maly.services.RetrieverService;
import sk.stuba.fei.dp.maly.ui.models.IndividualsDatatableModel;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Patrik on 04/01/2017.
 */
@Controller
@RequestMapping(path = "/retriever")
public class RetrieverController {

    private OntologyService ontologyService;
    private RetrieverService instanceRetrieverService;

    @Autowired
    public RetrieverController(OntologyService service, RetrieverService rService)
    {
        this.ontologyService=service;
        this.instanceRetrieverService = rService;
    }

    @RequestMapping(value = "",method = RequestMethod.GET)
    public String getRetrieverPage(Model model,@RequestParam(value = "oid",required = false) Long ontologyId) {
        RetrieverDataRequestDto config = new RetrieverDataRequestDto();
        if(ontologyId != null){
            config.setOntologyId(ontologyId);
        }
        model.addAttribute("configuration",config);
        model.addAttribute("retrieverAnswer",new LinkedList<IndividualsDatatableModel>());
        return "retriever";
    }

    @RequestMapping(value = "",method = RequestMethod.POST)
    public String getRetrieverAnswer(@ModelAttribute("configuration") RetrieverDataRequestDto configuration, BindingResult bindingResult,Model model) {
        long start = System.currentTimeMillis();
        if(bindingResult.hasErrors()){
            for(ObjectError e: bindingResult.getAllErrors()){
                System.out.println(e.toString());
                //TODO : Display an error and handle the exception
            }
        }
        List<IndividualsDatatableModel> result = null;
        try {
            result = instanceRetrieverService.getIndividuals(configuration);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            model.addAttribute("error",true);
        }

        model.addAttribute("retrieverAnswer",result == null ? new LinkedList<>() : result );
        model.addAttribute("emptyAnswer",result != null ? result.isEmpty() : false);
        model.addAttribute("answeredQuery",configuration.getQuery());
        configuration.setQuery("");
        model.addAttribute("configuration",configuration);
        long end = System.currentTimeMillis();
        model.addAttribute("duration",(end-start) / 1000.0);
        return "retriever";
    }

    @ModelAttribute("ontologies")
    public List<Ontology> getAllOntologies(){
        return ontologyService.findAll();
    }

    @ModelAttribute("reasoners")
    public List<String> getSupportedReasoners(){
        List<String>result=new LinkedList<String>();
        for(int i=0;i<ReasonerImplementation.values().length;i++){
            result.add(ReasonerImplementation.values()[i].name());
        }
        result.add("CWA Reasoner");
        return result;
    }
}
