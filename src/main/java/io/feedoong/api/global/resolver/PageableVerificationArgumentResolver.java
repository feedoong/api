package io.feedoong.api.global.resolver;

import io.feedoong.api.global.exception.CustomException;
import io.feedoong.api.global.exception.ErrorCode;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class PageableVerificationArgumentResolver extends PageableHandlerMethodArgumentResolver {

    private static final int MINIMUM_PAGE_NUMBER = 0;
    private static final int MINIMUM_PAGE_SIZE = 1;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return super.supportsParameter(parameter);
    }

    @Override
    public Pageable resolveArgument(MethodParameter methodParameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        validatePageNumber(methodParameter, webRequest);
        validatePageSize(methodParameter, webRequest);

        return super.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);
    }

    private void validatePageNumber(MethodParameter methodParameter, NativeWebRequest webRequest) {
        String page = webRequest.getParameter(getParameterNameToUse(getPageParameterName(), methodParameter));
        if (Integer.parseInt(page) < MINIMUM_PAGE_NUMBER) {
            throw new CustomException(ErrorCode.INVALID_PAGE_NUMBER);
        }
    }

    private void validatePageSize(MethodParameter methodParameter, NativeWebRequest webRequest) {
        String size = webRequest.getParameter(getParameterNameToUse(getSizeParameterName(), methodParameter));
        if (Integer.parseInt(size) < MINIMUM_PAGE_SIZE) {
            throw new CustomException(ErrorCode.INVALID_PAGE_SIZE);
        }
    }
}
