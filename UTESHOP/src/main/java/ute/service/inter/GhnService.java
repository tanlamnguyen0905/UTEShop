package ute.service.inter;

import java.util.Map;

public interface GhnService {
	/**
	 * Lấy danh sách tỉnh/thành phố
	 */
	Map<String, Object> getProvinces();
	
	/**
	 * Lấy danh sách quận/huyện theo tỉnh
	 * @param provinceId ID của tỉnh/thành phố
	 */
	Map<String, Object> getDistricts(int provinceId);
	
	/**
	 * Lấy danh sách phường/xã theo quận
	 * @param districtId ID của quận/huyện
	 */
	Map<String, Object> getWards(int districtId);
}

