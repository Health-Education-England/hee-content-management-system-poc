package uk.nhs.hee.web.security.web.servlet.support;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import uk.nhs.hee.web.security.config.oauth2.azure.AzureOAuth2ClientSecurityConfig;

@Order(1)
public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer  {

    @Override
    public void onStartup(@NotNull ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
        /*
         * Needed to support a "request" scope in Spring Security filters,
         * since they're configured as a Servlet Filter. But not necessary
         * if they're configured as interceptors in Spring MVC.
         */
        servletContext.addListener(new RequestContextListener());
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] {
                AzureOAuth2ClientSecurityConfig.class
        };
    }

    @Override
    protected String @NotNull [] getServletMappings() {
        return new String[] { "/" };
    }

}
