package com.project.elisa.advice;

import com.project.elisa.annotation.Authorized;
import com.project.elisa.exceptions.NotAuthorizedException;
import com.project.elisa.exceptions.NotLoggedInException;
import com.project.elisa.models.Role;
import com.project.elisa.models.User;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

@Aspect
@Component
public class AuthAspect {
    @Autowired
    private HttpServletRequest req;

    @Around("@annotation(authorized)")
    public Object authenticate(ProceedingJoinPoint pjp, Authorized authorized) throws Throwable{
        HttpSession session = req.getSession(false);

        session.setAttribute("name","leo");

        String name = (String) session.getAttribute("name");
        if(session == null || session.getAttribute("currentUser") == null){
            throw new NotLoggedInException("WARNING: PLEASE BE LOGGED IN!");
        }

        User currentUser = (User) session.getAttribute("currentUser");
        Role currentRole = currentUser.getRole();

        List<Role> allowedRoles = Arrays.asList(authorized.allowedRoles());

        if(!allowedRoles.contains(currentRole)){
            throw new NotAuthorizedException("WARNING: AUTHORITY NOT GRANTED!");
        }

        return pjp.proceed(pjp.getArgs());
    }
}
