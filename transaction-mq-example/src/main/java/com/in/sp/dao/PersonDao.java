package com.in.sp.dao;

import com.in.sp.bean.Person;

import org.apache.ibatis.annotations.Mapper;

/**
 * @author Ship
 * @date 2020-04-27 19:09
 */
@Mapper
public interface PersonDao {

    int insert(Person person);
}
