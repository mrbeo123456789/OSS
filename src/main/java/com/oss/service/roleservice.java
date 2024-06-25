package com.oss.service;

import com.oss.model.Role;
import com.oss.repository.RoleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class roleservice {
    @Autowired
    private RoleRepository roleRepository;
    public Role getRoleById(Long roleId) {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));
    }
    public List<Role> getListRole() {
        return roleRepository.findByRoleNameNot("admin");
    }

}
