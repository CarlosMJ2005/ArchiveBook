
package com.archivebook.archivebook.repository;

import com.archivebook.archivebook.entities.Editorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EditorialRepository extends JpaRepository<Editorial, Long>{
    
}
