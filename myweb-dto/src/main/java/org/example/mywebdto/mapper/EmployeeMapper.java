package org.example.mywebdto.mapper;

import org.example.mywebdto.dto.EmployeeDto;
import org.example.mywebdto.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

    default EmployeeDto toDto(Employee employee) {
        if (employee == null) return null;

        String fullName = employee.getFirstName() + " " + employee.getLastName();
        return new EmployeeDto(
                employee.getId(),
                fullName,
                employee.getEmail()
        );
    }

    default Employee toEntity(EmployeeDto dto) {
        if (dto == null) return null;

        String[] parts = dto.getFullName().split(" ", 2);
        String firstName = parts.length > 0 ? parts[0] : "";
        String lastName = parts.length > 1 ? parts[1] : "";

        return new Employee(
                dto.getId(),
                firstName,
                lastName,
                dto.getEmail()
        );
    }
}
