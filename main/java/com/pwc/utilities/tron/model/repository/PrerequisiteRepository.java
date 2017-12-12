package com.pwc.utilities.tron.model.repository;

import org.springframework.data.repository.CrudRepository;
import com.pwc.utilities.tron.model.entity.Patch;
import com.pwc.utilities.tron.model.entity.Prerequisite;

public interface PrerequisiteRepository extends CrudRepository<Prerequisite, Integer> {

}
