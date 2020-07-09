package uk.nhs.hee.web.component.helper;

import java.lang.reflect.Method;

import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.parameters.JcrPath;
import org.hippoecm.hst.core.parameters.Parameter;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.onehippo.cms7.essentials.components.CommonComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class containing component helper methods
 * (e.g. get HippoBean by given relative/absolute document path)
 */
public class ComponentHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComponentHelper.class);

    /**
     * Returns HippoBean by given relative/absolute {@code documentPath}.
     *
     * @param <T>              the type parameter
     * @param commonComponent  the {@link CommonComponent} instance
     * @param paramName        the param name
     * @param parametersInfo   the parameters info
     * @param documentPath     the document path
     * @param beanMappingClass the bean mapping class
     * @return
     */
    public static <T extends HippoBean> T getHippoBeanForPath(
            CommonComponent commonComponent,
            String paramName,
            ParametersInfo parametersInfo,
            String documentPath,
            Class<T> beanMappingClass) {

        Class<?> paramInfoClass = parametersInfo.type();
        JcrPath paramJcrPath = getJcrPath(paramName, paramInfoClass);

        if (paramJcrPath == null) {
            return null;
        }

        LOGGER.debug("Parameter '{}' uses '{}' JcrPath",
                paramName, paramJcrPath.isRelative() ? "relative" : "absolute");

        if (paramJcrPath.isRelative()) {
            return commonComponent.getHippoBeanForPath(documentPath, beanMappingClass);
        }

        LOGGER.debug("Document path of parameter '{}' => '{}'", paramName, documentPath);

        HippoBean rootHippoBean = null;
        try {
            rootHippoBean = (HippoBean) RequestContextProvider.get()
                    .getObjectBeanManager().getObject(paramJcrPath.pickerRootPath());
        } catch (ObjectBeanManagerException e) {
            LOGGER.error(
                    "Caught error '{}' while retrieving the object for the path '{}'",
                    e.getMessage(),
                    paramJcrPath.pickerRootPath(),
                    e);
            return null;
        }

        String relativeDocumentPath = documentPath.substring(paramJcrPath.pickerRootPath().length() + 1);

        return rootHippoBean.getBean(relativeDocumentPath, beanMappingClass);
    }

    /**
     * Returns {@link JcrPath} for the given {@code paramName}.
     *
     * @param paramName the name of the {@link Parameter} whose {@link JcrPath} needs to be returned.
     * @param paramInfoClass the component info class.
     * @return the {@link JcrPath} for the given {@code paramName}
     */
    private static JcrPath getJcrPath(String paramName, Class<?> paramInfoClass) {
        for (Method method : paramInfoClass.getMethods()) {
            Parameter parameter = method.getAnnotation(Parameter.class);

            if (parameter != null && parameter.name().equals(paramName)) {
                return method.getAnnotation(JcrPath.class);
            }
        }
        return null;
    }

}
