package ute.service.admin.inter;

import ute.entities.Voucher;
import java.util.List;

public interface VoucherService {
    void insert(Voucher voucher);
    void update(Voucher voucher);
    void delete(Voucher voucher);
    void delete(Long id);

    List<Voucher> findByCodeVoucherPaginated(String codeVoucher, int firstResult, int maxResults);
    long countByCodeVoucher(String codeVoucher);
    List<Voucher> findPage(int page, int size);
    long count();
    Voucher findById(Long id);
    Voucher findByCodeVoucherExact(String codeVoucher);
}