package sk.stuba.fei.dp.maly.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Patrik on 10/04/2017.
 */
@Service
@Transactional
public class AboutService {

    public void downloadAPI(String path,HttpServletRequest request,
                            HttpServletResponse response) throws IOException{
        File toDownload = new File(path);
        if(!toDownload.exists()){
            System.out.println(path + " does not exits");
            return;
        }

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
