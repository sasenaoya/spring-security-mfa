package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.config.MfaAuthentication;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Getter
@Setter
class MfaRequestParameter {

    private String code;
}

@RestController
@RequestMapping("/api")
@Slf4j
public class MfaResource {

    AuthenticationSuccessHandler successHandler;
    AuthenticationFailureHandler failureHandler;

    MfaResource(AuthenticationSuccessHandler successHandler, AuthenticationFailureHandler failureHandler) {
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
    }

    @PostMapping("/mfa")
    public ResponseEntity<Object> mfaLogin(
        @RequestBody MfaRequestParameter param,
        MfaAuthentication authentication,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        log.debug("mfaLogin");
        if (authentication.getFirst() == null) {
            throw new BadRequestAlertException("invalid code", "mfa", "mfa.invalid.code");
        } else if (param.getCode().equals("1234")) {
            SecurityContextHolder.getContext().setAuthentication(authentication.getFirst());
            return ResponseEntity.ok().build();
            // successHandler.onAuthenticationSuccess(request, response, authentication.getFirst());
        } else {
            // failureHandler.onAuthenticationFailure(request, response, new BadCredentialsException("bad credentials"));
            throw new BadRequestAlertException("invalid code", "mfa", "mfa.invalid.code");
        }
    }
}
