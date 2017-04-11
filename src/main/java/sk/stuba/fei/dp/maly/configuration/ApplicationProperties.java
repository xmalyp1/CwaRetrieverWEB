package sk.stuba.fei.dp.maly.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotNull;

/**
 * Created by Patrik on 27/12/2016.
 */
@ConfigurationProperties(prefix = "application")
@Configuration
@Getter
@Setter
public class ApplicationProperties {

    private String fileUploadRootFolder;
    private String fileUploadRootFolderInitStrategy;
    private String instanceRetrieverApiVersion;

    public String buildFilePath(@NotNull String relativePath) {
        return concatWithSlashes(fileUploadRootFolder, relativePath);
    }

    private String concatWithSlashes(String root, String relative) {
        final String rootSanitized = root.trim();
        final String relativeSanitized = relative.trim();
        if ( !rootSanitized.endsWith("/") && !relativeSanitized.startsWith("/") ) {
            return rootSanitized + "/" + relativeSanitized;
        }
        else if ( rootSanitized.endsWith("/") && relativeSanitized.startsWith("/") ) {
            return rootSanitized + relativeSanitized.substring(1);
        }
        return rootSanitized + relativeSanitized;
    }

}
