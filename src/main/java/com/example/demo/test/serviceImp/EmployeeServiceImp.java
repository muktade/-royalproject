package com.example.demo.test.serviceImp;

import com.example.demo.test.entity.Employee;
import com.example.demo.test.entity.Login;
import com.example.demo.test.repository.EmployeeRepository;
import com.example.demo.test.service.EmployeeService;
import com.example.demo.test.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
@RequiredArgsConstructor
public class EmployeeServiceImp implements EmployeeService {

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private LoginService loginService;

    @Override
    public Employee saveEmployee(Employee employee) {
        String pass = passwordEncoder.encode(employee.getPassword());
        employee.setPassword(pass);
        return employeeRepository.save(employee);
    }

    @Override
    public Employee findEmployee(String email) {
        return employeeRepository.findEmployeeByEmail(email);

    }

    @Override
    public Employee checkAuth(Employee employee) {
        Login login = new Login();
//        Map<String,String> result = new HashMap<String,String>();
        String res = "";
//        result.put("message", res);

        try {
            Employee emp = employeeRepository.findEmployeeByEmail(employee.getEmail());
            if (emp != null) {
                if (!passwordEncoder.matches(employee.getPassword(), emp.getPassword())) {
                    res = "Password is not match";
                } else if (passwordEncoder.matches(employee.getPassword(), emp.getPassword())) {
                    res = "Password is match";
                    Login dbLogin = loginService.getLoginInfo(employee.getId());
                    if (dbLogin != null) {
                        login.setLoginId(dbLogin.getLoginId());
                        emp.setLogInfo(dbLogin);
                    }
                    login.setEmployeeId(employee.getId());
                    login.setLoginTime(new Date());
                } else {
                    res = "something is not ok";
                }
                loginService.saveLogin(login);
                emp.setMsg(res);
                return emp;
            } else {
                res = "email is not found";
                employee.setMsg(res);
                return employee;
            }

        } catch (Exception e) {
            employee.setMsg("Sorry Something is not write.");
            return employee;
        }
    }
}
