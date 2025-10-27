package ute.service.admin.Impl;

import ute.dao.admin.Impl.VoucherDaoImpl;
import ute.dao.admin.inter.VoucherDao;
import ute.entities.Voucher;
import ute.service.admin.inter.VoucherService;
import java.util.List;

public class VoucherServiceImpl implements VoucherService {
    private VoucherDao voucherDao = new VoucherDaoImpl();

    @Override
    public void insert(Voucher voucher) {
        voucherDao.insert(voucher);
    }

    @Override
    public void update(Voucher voucher) {
        voucherDao.update(voucher);
    }

    @Override
    public void delete(Voucher voucher) {
        voucherDao.delete(voucher);
    }

    @Override
    public void delete(Long id) {
        voucherDao.delete(id);
    }

    @Override
    public Voucher findById(Long id) {
        return voucherDao.findById(id);
    }

    @Override
    public List<Voucher> findByCodeVoucherPaginated(String codeVoucher, int firstResult, int maxResults) {
        return voucherDao.findByCodeVoucherPaginated(codeVoucher, firstResult, maxResults);
    }

    @Override
    public long countByCodeVoucher(String codeVoucher) {
        return voucherDao.countByCodeVoucher(codeVoucher);
    }

    @Override
    public List<Voucher> findPage(int page, int size) {
        return voucherDao.findPage(page, size);
    }

    @Override
    public long count() {
        return voucherDao.count();
    }

    @Override
    public Voucher findByCodeVoucherExact(String codeVoucher) {
        return voucherDao.findByCodeVoucherExact(codeVoucher);
    }
}