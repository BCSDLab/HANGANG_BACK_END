package in.hangang.interceptor;


import in.hangang.annotation.Auth;
import in.hangang.enums.ErrorMessage;;
import in.hangang.exception.AccessTokenExpireException;
import in.hangang.exception.AccessTokenInvalidException;
import in.hangang.util.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private Jwt jwt;
    /*
    token.user.name
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) return true;
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        Auth auth= handlerMethod.getMethod().getDeclaredAnnotation(Auth.class);

        if ( auth != null ) {// auth 어노테이션이 있다면
            String accessToken = request.getHeader("Authorization");

            if ( accessToken != null) {
                int result = jwt.isValid(accessToken,0); // flag 0 -> access / 1 refresh

                if (result == 0) { // valid 하다면
                    return true;
                }
                else
                    throw new AccessTokenInvalidException(ErrorMessage.ACCESS_FORBIDDEN_AUTH_INVALID_EXCEPTION);
            }
            else{
                throw new AccessTokenInvalidException(ErrorMessage.ACCESS_FORBIDDEN_AUTH_INVALID_EXCEPTION); // jwt token이 null이라면
        }
        }


        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }
}
