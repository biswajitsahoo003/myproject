package com.qcm.hotel.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qcm.hotel.portal.entites.User;
import com.qcm.hotel.portal.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository repository;

	public UserBean create(UserBean userBean) {
		User userEntity=new User();
		userEntity.setFirstName(userBean.getFirstName());
		userEntity.setLastName(userBean.getLastName());
		userEntity.setEmail(userBean.getEmail());
		userBean = new UserBean(repository.save(userEntity));
		return userBean;
	}

	public UserBean delete(int id) {
		User user = findById(id);
		if (user != null) {
			repository.delete(user);
		}
		return new UserBean(user);
	}

	public List<UserBean> findAll() {
		List<UserBean> userBeanList = repository.findAll().stream().map(UserBean::new).collect(Collectors.toList());
		return userBeanList;
	}

	public User findById(int id) {
		return repository.findById(id);
	}

	public UserBean update(UserBean userBean) {
		User userEntity=new User();
		userEntity.setId(userBean.getId());
		userBean = new UserBean(repository.save(userEntity));
		return userBean;
	}

	public UserBean findOne(int id) {
		UserBean userBean=new UserBean(repository.findById(id));
		return userBean;
	}


}
