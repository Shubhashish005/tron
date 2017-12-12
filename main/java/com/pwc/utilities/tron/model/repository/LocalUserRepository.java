package com.pwc.utilities.tron.model.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.pwc.utilities.tron.model.entity.LocalUser;

public interface LocalUserRepository extends CrudRepository<LocalUser, Integer>{

	@Query("select u from LocalUser u where u.guid = ?1")
	  LocalUser getLocaluserFromGuid(String guid);
	
}
