package com.erp.app.repository;
import java.awt.print.Pageable;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.erp.app.entities.Members;

@Repository
public interface SupabaseRepo extends JpaRepository<Members, Integer> {

	@Query("SELECT COUNT(m) FROM Members m")
    long getTotalMemberCount();
	
	@Query("SELECT COUNT(DISTINCT m.वार्ड_संख्या) FROM Members m")
	long getTotalWard();

	@Query(value = "SELECT * FROM members ORDER BY सदस्य_नंबर DESC LIMIT 3", nativeQuery = true)
	List<Members> findLastThreeMembersNative();
	
	Members findByEmail(String email);
	
	@Query("SELECT m.वार्ड_संख्या, COUNT(m) FROM Members m GROUP BY m.वार्ड_संख्या")
    List<Object[]> getWardWiseCountRaw();
}
