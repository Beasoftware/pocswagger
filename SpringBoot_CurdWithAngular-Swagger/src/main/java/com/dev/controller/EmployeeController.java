package com.dev.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.exception.ResourceNotFoundException;
import com.dev.model.Employee;
import com.dev.repository.EmployeeRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/")
@Api(value = "Employee Management System", description = "Operations pertaining to employee in Employee Management System")
public class EmployeeController {
	@Autowired
	private EmployeeRepository employeeRepository;

	// get all employees
	@ApiOperation(value = "View a list of available employees", response = List.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved list"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@GetMapping("/employees")
	public List<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}

	// create employee rest api
	@ApiOperation(value = "Add an employee")
	@PostMapping("/employees")
	public Employee createEmployee(
			@ApiParam(value = "Employee object store in database table", required = true) @RequestBody Employee employee) {
		return employeeRepository.save(employee);
	}

	/*
	 * /// for adding multiple details
	 * 
	 * @PostMapping("/multipleEmpSave") public String createEmployee(@RequestBody
	 * List<Employee> employee) { employeeRepository.saveAll(employee); return
	 * "Record is saved Succssfully !";
	 * 
	 * }
	 */
	// get employee by id rest api
	@ApiOperation(value = "Get an employee by Id")
	@GetMapping("/employees/{id}")
	public ResponseEntity<Employee> getEmployeeById(
			@ApiParam(value = "Employee id from which employee object will retrieve", required = true) @PathVariable Long id) {
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id :" + id));
		return ResponseEntity.ok(employee);
	}

	// update employee rest api
	@ApiOperation(value = "Update an employee")
	@PutMapping("/employees/{id}")
	public ResponseEntity<Employee> updateEmployee(
			@ApiParam(value = "Employee Id to update employee object", required = true) @PathVariable Long id,
			@ApiParam(value = "Update employee object", required = true) @RequestBody Employee employeeDetails) {
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id :" + id));

		employee.setFirstName(employeeDetails.getFirstName());
		employee.setLastName(employeeDetails.getLastName());
		employee.setEmailId(employeeDetails.getEmailId());

		Employee updatedEmployee = employeeRepository.save(employee);
		return ResponseEntity.ok(updatedEmployee);
	}

	// delete employee rest api
	@ApiOperation(value = "Delete an employee")
	@DeleteMapping("/employees/{id}")
	public ResponseEntity<Map<String, Boolean>> deleteEmployee(
			@ApiParam(value = "Employee Id from which employee object will delete from database table", required = true) @PathVariable Long id) {
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id :" + id));

		employeeRepository.delete(employee);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return ResponseEntity.ok(response);
	}

}// end class
