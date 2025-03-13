package controller;

import dao.PlayerDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Player;

import java.io.IOException;

@WebServlet("/updatePlayer")
public class UpdatePlayerServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Set UTF-8 để hỗ trợ tiếng Việt
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            // Lấy dữ liệu từ form
            int playerId = Integer.parseInt(request.getParameter("playerId"));
            String name = request.getParameter("name");
            String fullName = request.getParameter("fullName");
            String ageStr = request.getParameter("age");
            String indexIdStr = request.getParameter("indexId");

            // Kiểm tra dữ liệu nhập vào
            if (name == null || fullName == null || ageStr == null || indexIdStr == null ||
                    name.trim().isEmpty() || fullName.trim().isEmpty()) {
                response.sendRedirect("index.jsp?error=Thiếu dữ liệu nhập vào");
                return;
            }

            int age = Integer.parseInt(ageStr);
            int indexId = Integer.parseInt(indexIdStr);

            // Tạo đối tượng Player và cập nhật vào DB
            Player player = new Player(playerId, name, fullName, age, indexId);
            boolean success = PlayerDAO.updatePlayer(player);

            // Redirect về index.jsp với thông báo
            if (success) {
                response.sendRedirect("index.jsp?success=Cập nhật thành công");
            } else {
                response.sendRedirect("index.jsp?error=Cập nhật thất bại");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("index.jsp?error=Dữ liệu không hợp lệ");
        }
    }
}
