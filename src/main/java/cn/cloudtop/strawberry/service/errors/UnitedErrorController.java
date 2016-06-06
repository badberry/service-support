package cn.cloudtop.strawberry.service.errors;

import cn.cloudtop.strawberry.rest.FieldValidError;
import cn.cloudtop.strawberry.rest.RestException;
import cn.cloudtop.strawberry.rest.RestResponse;
import cn.cloudtop.strawberry.rest.ValidResponse;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by jackie on 16-4-25
 * 统一异常处理.
 */
@Controller
@RequestMapping("error")
public class UnitedErrorController implements ErrorController {

    private static final String ERROR_ATTRIBUTE = "org.springframework.boot.autoconfigure.web.DefaultErrorAttributes.ERROR";
    private static final Logger LOGGER = LoggerFactory.getLogger(UnitedErrorController.class);

    @Override
    public String getErrorPath() {
        return "/error";
    }

    private Throwable getError(RequestAttributes requestAttributes) {
        Throwable exception = getAttribute(requestAttributes, ERROR_ATTRIBUTE);
        if (exception == null) {
            exception = getAttribute(requestAttributes, "javax.servlet.error.exception");
        }
        return exception;
    }

    @SuppressWarnings("unchecked")
    private <T> T getAttribute(RequestAttributes requestAttributes, String name) {
        return (T) requestAttributes.getAttribute(name, RequestAttributes.SCOPE_REQUEST);
    }

    @RequestMapping(produces = "text/html")
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(getStatus(request).value());
        //todo:jackie return 404 and 500 view.
        return new ModelAndView("error");
    }

    @RequestMapping
    @ResponseBody
    public RestResponse error(HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(200);
        if (getStatus(request) == HttpStatus.NOT_FOUND) {
            LOGGER.warn("no handle matched for this request.");
            return new ErrorRestResponse(BasicErrorCode.Api_Not_Existed, "API Not Found.");
        } else {
            RequestAttributes requestAttributes = new ServletRequestAttributes(request);
            Throwable throwable = getError(requestAttributes);
            if (throwable != null) {
                return handlerException(throwable);
            }
            Object message = getAttribute(requestAttributes, "javax.servlet.error.message");
            LOGGER.error(StringUtils.isEmpty(message) ? "unknown error." : message.toString());
            return new ErrorRestResponse(BasicErrorCode.Unknown_Error, "occur unknown error.");
        }
    }

    private RestResponse handlerException(Throwable throwable) {
        if (throwable instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException ex = (MethodArgumentNotValidException) throwable;
            List<FieldValidError> fieldValidErrors = Lists.newArrayList();
            List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
            for (ObjectError objectError : allErrors) {
                if (objectError instanceof FieldError) {
                    FieldError fieldError = (FieldError) objectError;
                    fieldValidErrors.add(new FieldValidError(fieldError.getObjectName(), fieldError.getField(),
                            fieldError.getRejectedValue(), fieldError.getDefaultMessage()));
                }
            }
            LOGGER.error(ex.getMessage());
            return new ValidResponse(ex.getParameter().getParameterName(), fieldValidErrors);
        } else if (throwable instanceof HttpRequestMethodNotSupportedException) {
            LOGGER.error(throwable.getMessage());
            return new ErrorRestResponse(BasicErrorCode.Method_Not_Support, throwable.getMessage());
        } else if (throwable instanceof RestException) {
            LOGGER.error(((RestException) throwable).getDebugMessage(), throwable);
            return new ErrorRestResponse(((RestException) throwable).getCode(), throwable.getMessage());
        }
        LOGGER.error("occur unknown exception.", throwable);
        return new ErrorRestResponse(BasicErrorCode.None_Processed_Exception, "none processed exception.");
    }

    protected HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request
                .getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        try {
            return HttpStatus.valueOf(statusCode);
        } catch (Exception ex) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
