package xbb.ai.erp.module.supplier.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.supplier.admin.dto.VendorListDTO;
import xbb.ai.erp.module.supplier.admin.dto.VendorSaveDTO;
import xbb.ai.erp.module.supplier.admin.vo.VendorDetailVO;
import xbb.ai.erp.module.supplier.admin.vo.VendorListItemVO;
import xbb.ai.erp.module.supplier.admin.vo.VendorSaveItemVO;
import xbb.ai.erp.module.supplier.application.assembler.VendorAdminAssembler;
import xbb.ai.erp.module.supplier.application.service.VendorAdminAppService;
import xbb.ai.erp.module.supplier.domain.model.Vendor;
import xbb.ai.erp.module.supplier.domain.repository.VendorRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class VendorAdminAppServiceImpl implements VendorAdminAppService {

    private final VendorRepository vendorRepository;

    @Override
    public ListBaseVO<VendorListItemVO> list(VendorListDTO dto) {
        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("id", dto.getId());
        conditionMap.put("corpid", dto.getCorpid());
        conditionMap.put("vendorCode", dto.getVendorCode());
        conditionMap.put("vendorName", dto.getVendorName());
        conditionMap.put("vendorShortName", dto.getVendorShortName());
        conditionMap.put("vendorCategory", dto.getVendorCategory());
        conditionMap.put("mainBusinessCategory", dto.getMainBusinessCategory());
        conditionMap.put("ownerPurchaserId", dto.getOwnerPurchaserId());
        conditionMap.put("bizStatus", dto.getBizStatus());
        conditionMap.put("refStatus", dto.getRefStatus());
        conditionMap.put("offset", dto.getOffset());
        conditionMap.put("pageSize", dto.getPageSize());
        conditionMap.put("groupByStr", dto.getGroupByStr());
        conditionMap.put("orderByStr", dto.getOrderByStr());
        List<Vendor> list = vendorRepository.findByCondition(conditionMap);
        Long total = vendorRepository.count(conditionMap);
        ListBaseVO<VendorListItemVO> vo = new ListBaseVO<>();
        vo.setList(list.stream().map(VendorAdminAssembler::toListItemVO).toList());
        vo.setPageHelper(new ListBaseVO.PageHelper(dto.getPageNum() == null ? 1 : dto.getPageNum(), total == null ? 0 : total.intValue()));
        return vo;
    }

    @Override
    public SaveItemVO<VendorSaveItemVO> addItem(BaseDTO dto) {
        SaveItemVO<VendorSaveItemVO> vo = new SaveItemVO<>();
        vo.setData(VendorAdminAssembler.buildEmptySaveItemVO());
        return vo;
    }

    @Override
    public SaveItemVO<VendorSaveItemVO> updateItem(IdBaseDTO dto) {
        SaveItemVO<VendorSaveItemVO> vo = new SaveItemVO<>();
        vo.setData(toSaveItem(dto));
        return vo;
    }

    @Override
    public Long save(VendorSaveDTO dto) {
        Vendor vendor = VendorAdminAssembler.toVendor(dto);
        if (vendor.getId() == null) {
            vendorRepository.insert(vendor);
        } else {
            vendorRepository.update(vendor);
        }
        return vendor.getId();
    }

    @Override
    public VendorDetailVO detail(IdBaseDTO dto) {
        return VendorAdminAssembler.toDetailVO(toSaveItem(dto));
    }

    @Override
    public void delete(BatchBaseDTO dto) {
        if (dto.getIdList() == null) {
            return;
        }
        dto.getIdList().forEach(id -> vendorRepository.removeById(dto.getCorpid(), id));
    }

    private VendorSaveItemVO toSaveItem(IdBaseDTO dto) {
        return VendorAdminAssembler.toSaveItemVO(vendorRepository.findById(dto.getCorpid(), dto.getId()));
    }
}
