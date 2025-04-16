package org.example.mywebdto.controller;

import org.example.mywebdto.dto.EmployeeDto;
import org.example.mywebdto.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Danh sách nhân viên
    @GetMapping
    public String listEmployees(Model model) {
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "employee/list"; // src/main/resources/templates/employee/list.html
    }

    // Form tạo mới
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("employee", new EmployeeDto());
        return "employee/create"; // src/main/resources/templates/employee/create.html
    }

    // Xử lý tạo mới
    @PostMapping
    public String createEmployee(@ModelAttribute("employee") EmployeeDto employeeDto) {
        employeeService.createEmployee(employeeDto);
        return "redirect:/employees";
    }

    // Form chỉnh sửa
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        EmployeeDto employeeDto = employeeService.getEmployeeById(id);
        model.addAttribute("employee", employeeDto);
        return "employee/edit"; // src/main/resources/templates/employee/edit.html
    }

    // Xử lý cập nhật
    @PostMapping("/update/{id}")
    public String updateEmployee(@PathVariable Long id, @ModelAttribute("employee") EmployeeDto employeeDto) {
        employeeService.updateEmployee(id, employeeDto);
        return "redirect:/employees";
    }

    // Xóa nhân viên
    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return "redirect:/employees";
    }

}
