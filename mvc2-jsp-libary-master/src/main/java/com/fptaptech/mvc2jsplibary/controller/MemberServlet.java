package com.fptaptech.mvc2jsplibary.controller;

import com.fptaptech.mvc2jsplibary.bean.MemberBean;
import com.fptaptech.mvc2jsplibary.dao.MemberDAO;
import com.fptaptech.mvc2jsplibary.entity.Member;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/members")
public class MemberServlet extends HttpServlet {

    private MemberDAO memberDAO; // Thay đổi tên biến để thống nhất

    @Override
    public void init() throws ServletException {
        memberDAO = new MemberDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "add":
                request.getRequestDispatcher("/WEB-INF/views/member/add.jsp").forward(request, response);
                break;
            case "edit":
                Long id = Long.parseLong(request.getParameter("id"));
                Member member = memberDAO.findById(id);
                if (member != null) {
                    MemberBean memberBean = mapMemberToBean(member);
                    request.setAttribute("member", memberBean);
                    request.getRequestDispatcher("/WEB-INF/views/member/edit.jsp").forward(request, response);
                } else {
                    response.sendRedirect(request.getContextPath() + "/members");
                }
                break;
            case "delete":
                memberDAO.delete(Long.parseLong(request.getParameter("id")));
                response.sendRedirect(request.getContextPath() + "/members");
                break;
            default:
                List<MemberBean> members = memberDAO.findAll().stream()
                        .map(this::mapMemberToBean)
                        .toList();
                request.setAttribute("members", members);
                request.getRequestDispatcher("/WEB-INF/views/member/list.jsp").forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        MemberBean memberBean = new MemberBean();

        memberBean.setFirstName(request.getParameter("firstName"));
        memberBean.setLastName(request.getParameter("lastName"));
        memberBean.setEmail(request.getParameter("email"));

        if ("edit".equals(action)) {
            memberBean.setId(Long.parseLong(request.getParameter("id")));
        }

        memberDAO.save(mapBeanToMember(memberBean));
        response.sendRedirect(request.getContextPath() + "/members");
    }

    private MemberBean mapMemberToBean(Member member) {
        MemberBean bean = new MemberBean();
        bean.setId(member.getId());
        bean.setFirstName(member.getFirstName());
        bean.setLastName(member.getLastName());
        bean.setEmail(member.getEmail());
        bean.setTotalBorrowedBooks(member.getLoans() != null ? member.getLoans().size() : 0);
        return bean;
    }

    private Member mapBeanToMember(MemberBean bean) {
        Member member = new Member();
        member.setId(bean.getId());
        member.setFirstName(bean.getFirstName());
        member.setLastName(bean.getLastName());
        member.setEmail(bean.getEmail());
        return member;
    }
}