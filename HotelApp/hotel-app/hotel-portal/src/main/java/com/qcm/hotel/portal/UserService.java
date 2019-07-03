package com.qcm.hotel.portal;

import java.util.List;

import com.qcm.hotel.portal.entites.User;

public interface UserService {

    UserBean create(UserBean user);

    UserBean delete(int id);

    List<UserBean> findAll();

    UserBean findOne(int id);

    UserBean update(UserBean user);
}
