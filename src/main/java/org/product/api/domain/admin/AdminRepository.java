package org.product.api.domain.admin;

import org.product.api.base.BaseRepository;

import java.util.Optional;

public interface AdminRepository extends BaseRepository<Admin, Long> {

    Optional<Admin> findFirstByCounselorIdAndDeletedFalse(String counselorId);
}
