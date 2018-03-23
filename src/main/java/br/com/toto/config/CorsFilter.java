package br.com.toto.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Configuration
public class CorsFilter implements Filter {

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {

  }

  @Override
  public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
      throws IOException, ServletException {

    HttpServletResponse response = (HttpServletResponse) resp;
    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setHeader("Access-Control-Allow-Credentials", "true");

    HttpServletRequest request = (HttpServletRequest) req;
    if ("OPTIONS".equals(request.getMethod())) {
      response.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, PUT, OPTIONS");
      response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept, "
          + "transactionId");
      response.setHeader("Access-Control-Max-Age", "3600");

      response.setStatus(HttpServletResponse.SC_OK);
    } else {
      chain.doFilter(req, resp);
    }

  }

  @Override
  public void destroy() {

  }
}

