package com.mycompany.myapp.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

class MfaRequiredException extends RuntimeException {

    private static final long serialVersionUID = 1L;
}

public class MfaAuthenticationHandler implements AuthenticationSuccessHandler, AuthenticationFailureHandler {

    private final AuthenticationSuccessHandler successHandler;

    public MfaAuthenticationHandler(String url) {
        SimpleUrlAuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler(url);
        successHandler.setAlwaysUseDefaultTargetUrl(true);
        this.successHandler = successHandler;
    }

    /**
     * Called when a user has been successfully authenticated.
     *
     * @param request        the request which caused the successful authentication
     * @param response       the response
     * @param authentication the <tt>Authentication</tt> object which was created
     *                       during
     *                       the authentication process.
     */
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
        throws IOException, ServletException {
        var p = (User) authentication.getPrincipal();
        if (p.getUsername().equals("user")) {
            var mfaAuthentication = new MfaAuthentication(authentication);
            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(mfaAuthentication);
            response.setStatus(HttpStatus.OK.value());
            // response.sendError(HttpStatus.OK.value(), "mfa required");
            // successHandler.onAuthenticationSuccess(request, response, mfaAuthentication);
        } else {
            response.setStatus(HttpStatus.OK.value());
        }
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
        throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
}
