package cn.singno.commonsframework.module.app.service;

import java.util.Set;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import cn.singno.commonsframework.module.app.entity.User;

@Validated
public interface UserService
{
	public User save(@Valid User user);

	public User findByUserName(String userName);

	public Set<String> findRoles(String userName);

	public Set<String> findPermissions(String userName);
}