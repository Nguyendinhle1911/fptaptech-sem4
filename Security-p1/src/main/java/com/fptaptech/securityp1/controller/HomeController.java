package com.fptaptech.securityp1.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.security.Principal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(@AuthenticationPrincipal Object principal, HttpServletRequest request, Model model) {
        String username = "Guest";
        Object authorities = null;
        Object userObject = null;

        if (principal != null) {
            if (principal instanceof org.springframework.security.core.userdetails.User userDetails) {
                username = userDetails.getUsername();
                authorities = userDetails.getAuthorities();
                userObject = userDetails;
            } else if (principal instanceof OAuth2User oAuth2User) {
                username = oAuth2User.getAttribute("email") != null ? oAuth2User.getAttribute("email") : oAuth2User.getName();
                authorities = oAuth2User.getAuthorities();
                userObject = oAuth2User;
            }
        }

        model.addAttribute("username", username);
        model.addAttribute("authorities", authorities);
        model.addAttribute("userObject", userObject);

        HttpSession session = request.getSession(false);
        if (session != null) {
            model.addAttribute("sessionId", session.getId());
            model.addAttribute("creationTime", session.getCreationTime());
            model.addAttribute("lastAccessedTime", session.getLastAccessedTime());
            model.addAttribute("maxInactiveInterval", session.getMaxInactiveInterval());

            Map<String, Object> sessionAttrs = new HashMap<>();
            Enumeration<String> attrNames = session.getAttributeNames();
            while (attrNames.hasMoreElements()) {
                String attr = attrNames.nextElement();
                sessionAttrs.put(attr, session.getAttribute(attr));
            }
            model.addAttribute("sessionAttrs", sessionAttrs);
        } else {
            model.addAttribute("sessionId", "Không có session");
        }

        return "index";
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model, Principal principal) {
        if (principal != null) {
            return "redirect:/";
        }

        if (request.getParameter("error") != null) {
            model.addAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng");
        }
        if (request.getParameter("logout") != null) {
            model.addAttribute("message", "Đăng xuất thành công");
        }
        if (request.getParameter("expired") != null) {
            model.addAttribute("error", "Phiên đăng nhập đã hết hạn hoặc bị đăng nhập từ nơi khác.");
        }

        return "login";
    }
}
