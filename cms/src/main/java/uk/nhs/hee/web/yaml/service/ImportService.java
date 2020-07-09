package uk.nhs.hee.web.yaml.service;

import java.io.InputStream;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.hippoecm.hst.container.RequestContextProvider;
import org.onehippo.cm.ConfigurationService;
import org.onehippo.cms7.services.HippoServiceRegistry;

public class ImportService {
    private static final ConfigurationService configurationService;

    static {
        configurationService = HippoServiceRegistry.getService(ConfigurationService.class);
    }

    public static void importPlainYaml(InputStream yamlInputStream, String parentNodePath)
            throws RepositoryException {
        configurationService.importPlainYaml(yamlInputStream, getNode(parentNodePath));
    }


    public static Node getNode(String nodePath) throws RepositoryException {
        return RequestContextProvider.get().getSession().getNode(nodePath);
    }


}
