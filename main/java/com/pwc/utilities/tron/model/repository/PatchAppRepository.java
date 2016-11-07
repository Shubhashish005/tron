package com.pwc.utilities.tron.model.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.pwc.utilities.tron.model.entity.Patch;
import com.pwc.utilities.tron.model.entity.PatchApp;

public interface PatchAppRepository extends CrudRepository<PatchApp, Integer>{

	@Query("select u from PatchApp u where u.patch = ?1")
	  Iterable<PatchApp> getFilesForPatch(Patch patch);
	
}
