package com.project.elisa.services;

import com.project.elisa.exceptions.NotAuthorizedException;
import com.project.elisa.exceptions.NotLoggedInException;
import com.project.elisa.models.Role;
import com.project.elisa.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
public class AuthorizationService {
    @Autowired
    private HttpServletRequest req;

    public void guardByUserId(int userId) {

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("currentUser") == null) {
            throw new NotLoggedInException("YOU MUST LOG IN");
        }

        User currentUser = (User) session.getAttribute("currentUser");
        Role currentRole = currentUser.getRole();

        if (userId != currentUser.getUserId() && currentRole != Role.ADMIN) {
            throw new NotAuthorizedException
                    ("WARNING: YOU ARE NOT PERMITTED");
        }
    }
}
