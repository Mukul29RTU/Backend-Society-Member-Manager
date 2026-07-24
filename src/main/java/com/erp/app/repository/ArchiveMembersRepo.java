package com.erp.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.erp.app.entities.ArchiveMembers;
import com.erp.app.entities.Members;

@Repository
public interface ArchiveMembersRepo  extends JpaRepository<ArchiveMembers, Integer>{

	@Query("SELECT COUNT(m) FROM ArchiveMembers m")
    long getTotalMemberCount();
}
