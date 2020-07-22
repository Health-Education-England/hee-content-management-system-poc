package uk.nhs.hee.web.mockserver.service;

import org.onehippo.cms7.crisp.api.resource.Resource;
import org.onehippo.cms7.crisp.api.resource.ResourceException;
import uk.nhs.hee.web.mockserver.service.bean.Programme;
import uk.nhs.hee.web.mockserver.service.util.MockServerServiceBrokerUtil;

import java.util.ArrayList;
import java.util.List;

public class ProgrammeService extends AbstractMockServerService {

    protected ProgrammeService(MockServerServiceBrokerUtil resourceServiceBrokerUtil) {
        super(resourceServiceBrokerUtil);
    }

    public List<Programme> getProgrammesBySpecialty(String specialty) throws ResourceException {
        return getProgrammes("/search/programmes?specialty=" + specialty);
    }

    public List<Programme> getProgrammesBySubSpecialty(String subSpecialty) throws ResourceException {
        return getProgrammes("/search/programmes?subspecialty=" + subSpecialty);
    }

    public Programme getProgramme(String baseAbsPath) throws ResourceException {
        return getResourceServiceBrokerUtil().getProgramme(baseAbsPath);
    }

    private List<Programme> getProgrammes(String baseAbsPath) throws ResourceException {
        Resource resource = getResourceServiceBrokerUtil().getResource(baseAbsPath);

        if (!resource.isAnyChildContained()) {
            return null;
        }

        Resource programmesResource = (Resource) resource.getValue("programmes");

        List<Programme> programmes = new ArrayList<>();
        String baseUri = System.getProperty("base.uri");
        for (Resource programmeResource : programmesResource.getChildren()) {
            String programmeRef = programmeResource.getValue("$ref").toString();

            Programme programme = getResourceServiceBrokerUtil().getProgramme(programmeRef);

            if (programme == null) {
                continue;
            }

            programme.setRegionPageUrl(baseUri + programme.getRegionPageUrl());
            programme.getKeyStaffs().forEach(staff -> staff.setImageUrl(baseUri + staff.getImageUrl()));
            programmes.add(programme);
        }

        return programmes;
    }

}
