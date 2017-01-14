package sk.stuba.fei.dp.maly.services;

import org.dllearner.core.ComponentInitException;
import org.dllearner.reasoning.ReasonerImplementation;
import org.semanticweb.owlapi.model.OWLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.stuba.fei.dp.maly.exceptions.RetrieverException;
import sk.stuba.fei.dp.maly.persistence.dto.RetrieverDataRequestDto;
import sk.stuba.fei.dp.maly.persistence.entities.Ontology;
import sk.stuba.fei.dp.maly.retriever.InstanceRetriever;
import sk.stuba.fei.dp.maly.retriever.RetrieverConfiguration;
import sk.stuba.fei.dp.maly.ui.models.IndividualsDatatableModel;

import java.io.File;
import java.security.cert.CertPathValidatorException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Patrik on 05/01/2017.
 */
@Service
@Transactional
public class RetrieverService {

    private OntologyService ontologyService;

    @Autowired
    public RetrieverService(OntologyService service){
        this.ontologyService = service;
    }

    public List<IndividualsDatatableModel> getIndividuals(RetrieverDataRequestDto data) throws RetrieverException {
        Ontology o = ontologyService.findById(data.getOntologyId());

        if(o == null)
            throw new RetrieverException("The ontology was not found in the database.");

        InstanceRetriever instanceRetriever = new InstanceRetriever();
        RetrieverConfiguration config;
        try {
            config = new RetrieverConfiguration(instanceRetriever.createOntology(new File(o.getDestinationFile())));
        } catch (OWLException e) {
            throw new RetrieverException("Unable to load the ontology by the instance retriever", e);
        }
        if(config!=null || config.getOntology()==null){
            config.setCwaMode(data.isCwaMode());
            ReasonerImplementation reasoner = ReasonerImplementation.valueOf(data.getReasonerName());
            if(reasoner != null){
                config.setReasoner(reasoner);
            }else{
                throw new RetrieverException("The reasoner is not supported!");
            }

            try {
                return instanceRetriever.getIndividuals(data.getQuery(),config);
            } catch (ComponentInitException e) {
                throw new RetrieverException("Unable to initialize the retriever.",e);
            }

        }else{
            throw new RetrieverException("Unable to load the configuration");
        }
    }

}
