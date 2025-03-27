// src/main/java/com/example/salarymanagement/controller/EmployeeController.java
package org.example.salarymanagement.controller;

import jakarta.validation.Valid;
import org.example.salarymanagement.model.Employee;
import org.example.salarymanagement.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/")
    public String showHomePage(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "index";
    }

    @PostMapping("/add")
    public String addEmployee(@Valid @ModelAttribute("employee") Employee employee, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("employees", employeeService.getAllEmployees());
            return "index";
        }

        // Kiểm tra trùng tên
        if (employeeService.getEmployeeByName(employee.getName()) != null && employee.getId() == null) {
            model.addAttribute("error", "Unable to create. A user with name " + employee.getName() + " already exists.");
            model.addAttribute("employees", employeeService.getAllEmployees());
            return "index";
        }

        employeeService.saveEmployee(employee);
        model.addAttribute("success", employee.getId() == null ? "User created successfully" : "User updated successfully");
        model.addAttribute("employees", employeeService.getAllEmployees());
        model.addAttribute("employee", new Employee());
        return "index";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Employee employee = employeeService.getEmployeeById(id);
        if (employee == null) {
            model.addAttribute("error", "Employee not found");
            return "index";
        }
        model.addAttribute("employee", employee);
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "index";
    }

    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable("id") Long id, Model model) {
        employeeService.deleteEmployee(id);
        model.addAttribute("success", "User deleted successfully");
        model.addAttribute("employee", new Employee());
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "index";
    }
}