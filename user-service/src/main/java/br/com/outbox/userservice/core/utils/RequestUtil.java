package br.com.outbox.userservice.core.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static br.com.outbox.userservice.core.utils.Constants.EMPTY;
import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@UtilityClass
public class RequestUtil {

    public String getCurrentHttpMethod() {
        var request = getCurrentHttpRequest();
        if (!isEmpty(request)) {
            return getCurrentHttpRequest().getMethod();
        } else {
            return EMPTY;
        }
    }

    public String getCurrentUrl() {
        var request = getCurrentHttpRequest();
        if (!isEmpty(request)) {
            return getCurrentHttpRequest().getRequestURL().toString();
        } else {
            return EMPTY;
        }
    }

    private HttpServletRequest getCurrentHttpRequest(){
        try {
            var requestAttributes = RequestContextHolder.getRequestAttributes();
            return ((ServletRequestAttributes)requestAttributes).getRequest();
        } catch (Exception ex) {
            log.error("Not an HTTP request context.");
            return null;
        }
    }
}
