package controller;

import dao.PlayerDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Player;

import java.io.IOException;

@WebServlet("/addPlayer")
public class PlayerServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Xử lý UTF-8 để tránh lỗi tiếng Việt
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            // Lấy dữ liệu từ form
            String name = request.getParameter("name");
            String fullName = request.getParameter("fullName");
            String ageStr = request.getParameter("age");
            String indexIdStr = request.getParameter("indexId");

            // Kiểm tra dữ liệu đầu vào
            if (name == null || fullName == null || ageStr == null || indexIdStr == null ||
                    name.trim().isEmpty() || fullName.trim().isEmpty()) {
                response.sendRedirect("index.jsp?error=Thiếu dữ liệu nhập vào");
                return;
            }

            int age = Integer.parseInt(ageStr);
            int indexId = Integer.parseInt(indexIdStr);

            // Tạo đối tượng Player
            Player player = new Player(0, name, fullName, age, indexId);
            boolean success = PlayerDAO.addPlayer(player);

            // Kiểm tra thành công hay không
            if (success) {
                response.sendRedirect("index.jsp?success=Thêm cầu thủ thành công");
            } else {
                response.sendRedirect("index.jsp?error=Lỗi khi thêm cầu thủ");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("index.jsp?error=Dữ liệu không hợp lệ");
        }
    }
}
