package com.example.demo;

import ch.qos.logback.access.servlet.TeeFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.filter.OrderedFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

@Configuration
public class FilterConfiguration {

    @Bean
    public FilterRegistrationBean<TeeFilter> requestResponseLoggingFilter() {
        FilterRegistrationBean<TeeFilter> filterRegBean = new FilterRegistrationBean<>();

        // Option B) Replace the filter registration below
        filterRegBean.setFilter(new TeeFilter());
        // filterRegBean.setFilter(new MultipartSafeTeeFilter());           // This makes it work on Spring Boot 2.2

        filterRegBean.setName("Logback access-logging request response filter");
        filterRegBean.setOrder(OrderedFilter.REQUEST_WRAPPER_FILTER_MAX_ORDER);
        return filterRegBean;
    }

    /**
     * Workaround to force parsing of multipart request parts before request passes through TeeFilter
     *
     * @see <a href="https://jira.qos.ch/browse/LOGBACK-1503">Logback issue</a>
     */
    private static class MultipartSafeTeeFilter extends TeeFilter {
        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
            request.getParameter("whatever");
            super.doFilter(request, response, filterChain);
        }
    }
}