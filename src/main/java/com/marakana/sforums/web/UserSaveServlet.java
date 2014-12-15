
package com.marakana.sforums.web;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import com.marakana.sforums.dao.DuplicateIdException;
import com.marakana.sforums.domain.User;

public class UserSaveServlet extends AbstractDaoAccessServlet {

    private static final long serialVersionUID = -8403383569839821791L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<String> errors = new LinkedList<String>();
        req.setAttribute("errors", errors);
        User user = new User();
        req.setAttribute("user", user);

        String id = req.getParameter("id");
        if (!isEmpty(id)) {
            user.setId(new Long(id));
        }

        String firstName = req.getParameter("firstName");
        if (isEmpty(firstName)) {
            errors.add("First name cannot be blank");
        } else {
            user.setFirstName(firstName);
        }
        String lastName = req.getParameter("lastName");
        if (isEmpty(lastName)) {
            errors.add("Last name cannot be blank");
        } else {
            user.setLastName(lastName);
        }
        user.setOrganization(req.getParameter("organization"));
        user.setTitle(req.getParameter("title"));

        String email = req.getParameter("email");
        if (isEmpty(email)) {
            errors.add("Email cannot be blank");
        } else {
            user.setEmail(email);
        }

        String password = req.getParameter("password");
        String passwordVerification = req.getParameter("passwordVerification");
        boolean passwordIsSet = !isEmpty(password);
        boolean passwordVerificationIsSet = !isEmpty(passwordVerification);
        if (!passwordIsSet) {
            if (passwordVerificationIsSet || !user.isIdSet()) {
                errors.add("Password cannot be blank");
            }
        } else if (!passwordVerificationIsSet) {
            if (passwordIsSet) {
                errors.add("Password verification cannot be blank");
            }
        } else if (!password.equals(passwordVerification)) {
            errors.add("Passwords do not match.");
        } else {
            user.setPasswordDigest(md5Digest(password));
        }

        if (errors.isEmpty()) {
            super.logger.debug("Saving user {}", user);
            try {
                super.getDaoRepository().getUserDao().save(user);
                req.setAttribute("success", Boolean.TRUE);
            } catch (DuplicateIdException e) {
                errors.add("Duplicate email: " + e.getMessage());
            }
        } else {
            super.logger.debug("Not saving user {}. Errors are present", user);
        }
        req.getRequestDispatcher("/WEB-INF/jsp/userForm.jsp").forward(req, resp);
    }

    private static String md5Digest(String in) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] digest = messageDigest.digest(in.getBytes());
            return DatatypeConverter.printHexBinary(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to get MD5 digest", e);
        }
    }
}
