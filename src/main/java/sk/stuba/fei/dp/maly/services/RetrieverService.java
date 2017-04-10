package sk.stuba.fei.dp.maly.services;

import org.dllearner.core.ComponentInitException;
import org.dllearner.reasoning.ReasonerImplementation;
import org.semanticweb.owlapi.model.OWLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.stuba.fei.dp.maly.exceptions.InstanceRetrieverConfigException;
import sk.stuba.fei.dp.maly.exceptions.ManchesterSyntaxParseException;
import sk.stuba.fei.dp.maly.exceptions.RetrieverException;
import sk.stuba.fei.dp.maly.model.dto.InstanceDTO;
import sk.stuba.fei.dp.maly.persistence.dto.RetrieverDataRequestDto;
import sk.stuba.fei.dp.maly.persistence.dto.RetrieverResultDto;
import sk.stuba.fei.dp.maly.persistence.dto.RetrievingMode;
import sk.stuba.fei.dp.maly.persistence.entities.Ontology;
import sk.stuba.fei.dp.maly.retriever.InstanceRetriever;
import sk.stuba.fei.dp.maly.retriever.RetrieverConfiguration;
import sk.stuba.fei.dp.maly.retriever.RetrieverMode;
import sun.security.jca.GetInstance;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Patrik on 05/01/2017.
 */
@Service
@Transactional
public class RetrieverService {

    private OntologyService ontologyService;

    @Autowired
    public RetrieverService(OntologyService service) {
        this.ontologyService = service;
    }

    public List<RetrieverResultDto> getIndividuals(RetrieverDataRequestDto data) throws RetrieverException, ManchesterSyntaxParseException, InstanceRetrieverConfigException, ComponentInitException {

        Ontology o = ontologyService.findById(data.getOntologyId());

        if (o == null)
            throw new RetrieverException("The ontology was not found in the database.");

        InstanceRetriever instanceRetriever = new InstanceRetriever();
        RetrieverConfiguration config;
        try {
            config = new RetrieverConfiguration(instanceRetriever.createOntology(new File(o.getDestinationFile())));
        } catch (OWLException e) {
            throw new RetrieverException("Unable to load the ontology by the instance retriever", e);
        }
        if (config != null || config.getOntology() == null) {
            config.setMode(data.getMode() == RetrievingMode.CWA ? RetrieverMode.CWA : RetrieverMode.OWA);
            ReasonerImplementation reasoner = ReasonerImplementation.valueOf(data.getReasonerName());
            if (reasoner != null) {
                config.setReasoner(reasoner);
            } else {
                throw new RetrieverException("The reasoner is not supported!");
            }
            instanceRetriever.initializeRetriever(config);
            List<RetrieverResultDto> result=new ArrayList<>();
            try {
                List<InstanceDTO> instances = instanceRetriever.getIndividuals(data.getQuery());
                for(InstanceDTO dto : instances){
                    result.add(createResultDto(dto,config.getMode()));
                }
                return result;
            } catch (ComponentInitException e) {
                throw new RetrieverException("Unable to initialize the retriever.", e);
            } catch (ManchesterSyntaxParseException e) {
                throw new ManchesterSyntaxParseException("Unable to parse the expression : " + data.getQuery() + " .", e);
            } catch (InstanceRetrieverConfigException e) {
                throw new InstanceRetrieverConfigException("Instance retriever is not configure correctly. ", e);
            }

        } else {
            throw new RetrieverException("Unable to load the configuration");
        }
    }

    public List<RetrieverResultDto> getIndividualsInCompareMode(RetrieverDataRequestDto data) throws RetrieverException, InstanceRetrieverConfigException, ManchesterSyntaxParseException {
        Ontology o = ontologyService.findById(data.getOntologyId());

        if (o == null)
            throw new RetrieverException("The ontology was not found in the database.");

        InstanceRetriever instanceRetriever = new InstanceRetriever();
        RetrieverConfiguration config;
        try {
            config = new RetrieverConfiguration(instanceRetriever.createOntology(new File(o.getDestinationFile())));
        } catch (OWLException e) {
            throw new RetrieverException("Unable to load the ontology by the instance retriever", e);
        }
        if (config != null || config.getOntology() == null) {
            config.setMode(data.getMode() == RetrievingMode.CWA ? RetrieverMode.CWA : RetrieverMode.OWA);
            ReasonerImplementation reasoner = ReasonerImplementation.valueOf(data.getReasonerName());
            if (reasoner != null) {
                config.setReasoner(reasoner);
            } else {
                throw new RetrieverException("The reasoner is not supported!");
            }
            try {
                instanceRetriever.initializeRetriever(config);
            } catch (ComponentInitException e) {
                throw new RetrieverException("Unable to initialize the retriever.", e);
            } catch (InstanceRetrieverConfigException e) {
                throw new InstanceRetrieverConfigException("Instance retriever is not configure correctly. ", e);
            }
            return transformCompareModeResults(instanceRetriever.getIndividualsInCompareMode(data.getQuery()));
        } else {
            throw new RetrieverException("Unable to load the configuration");
        }
    }

    private RetrieverResultDto createResultDto(InstanceDTO instance,RetrieverMode mode){
        RetrieverResultDto retrieverResultDto = new RetrieverResultDto();
        retrieverResultDto.setIndividualClass(instance.getIndividualClass());
        retrieverResultDto.setNamedIndividual(instance.getNamedIndividual());
        retrieverResultDto.setCwa(RetrieverMode.CWA.equals(mode));
        return retrieverResultDto;
    }

    private List<RetrieverResultDto> transformCompareModeResults(Map<RetrieverMode,List<InstanceDTO>> compareMode){
        List<RetrieverResultDto> results = new ArrayList<>();
        for(InstanceDTO instance : compareMode.get(RetrieverMode.OWA)){
            results.add(createResultDto(instance,RetrieverMode.OWA));
        }

        for(InstanceDTO instance : compareMode.get(RetrieverMode.CWA)){
            RetrieverResultDto rrd = createResultDto(instance,RetrieverMode.OWA);
            if(!results.contains(rrd)){
                rrd.setCwa(true);
                results.add(rrd);
            }
        }
        return results;
    }
}
