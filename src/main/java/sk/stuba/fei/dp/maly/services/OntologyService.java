package sk.stuba.fei.dp.maly.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.stuba.fei.dp.maly.persistence.dao.OntologyDAO;
import sk.stuba.fei.dp.maly.persistence.entities.Ontology;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

/**
 * Created by Patrik on 28/12/2016.
 */
@Service
@Transactional
public class OntologyService {

    private OntologyDAO ontologyDAO;

    @Autowired
    public OntologyService (OntologyDAO ontologyDAO){
        this.ontologyDAO = ontologyDAO;
    }

    public List<Ontology> findAll(){
        return ontologyDAO.findAll();
    }

    public void create(Ontology ontology){
        ontologyDAO.persist(ontology);
    }

    public Ontology findById(Long id){
        return ontologyDAO.findById(id);
    }

    public void delete(Ontology ontology){
        ontologyDAO.delete(ontology);
    }

    public void downloadOntology(Ontology ontology,HttpServletRequest request,
                                 HttpServletResponse response) throws IOException {

        if(ontology != null){
            File toDownload = new File(ontology.getDestinationFile());
            ServletContext context = request.getServletContext();
            FileInputStream inputStream = null;
            inputStream = new FileInputStream(toDownload);

            // get MIME type of the file
            String mimeType = context.getMimeType(toDownload.getAbsolutePath());
            if (mimeType == null) {
                // set to binary type if MIME mapping not found
                mimeType = "application/octet-stream";
            }
            System.out.println("MIME type: " + mimeType);

            // set content attributes for the response
            response.setContentType(mimeType);
            response.setContentLength((int) toDownload.length());

            // set headers for the response
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"",
                    toDownload.getName());
            response.setHeader(headerKey, headerValue);

            // get output stream of the response
            OutputStream outStream = null;
            try {
                outStream = response.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            byte[] buffer = new byte[4096];
            int bytesRead = -1;


            // write bytes read from the input stream into the output stream
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
            outStream.flush();
            inputStream.close();
            outStream.close();

        }
    }
}
