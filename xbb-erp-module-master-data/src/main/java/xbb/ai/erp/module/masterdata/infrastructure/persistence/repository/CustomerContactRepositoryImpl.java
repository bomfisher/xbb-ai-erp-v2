package xbb.ai.erp.module.masterdata.infrastructure.persistence.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.masterdata.domain.model.CustomerContact;
import xbb.ai.erp.module.masterdata.domain.repository.CustomerContactRepository;
import xbb.ai.erp.module.masterdata.infrastructure.persistence.convertor.CustomerContactConvertor;
import xbb.ai.erp.module.masterdata.infrastructure.persistence.mapper.CustomerContactMapper;
import xbb.ai.erp.module.masterdata.infrastructure.persistence.po.CustomerContactPO;

@Repository
@RequiredArgsConstructor
public class CustomerContactRepositoryImpl implements CustomerContactRepository {
    private final CustomerContactMapper customerContactMapper;

    @Override
    public List<CustomerContact> findByCustomerId(String corpid, Long customerId) {
        return customerContactMapper.findByCustomerId(corpid, customerId).stream().map(CustomerContactConvertor::toDomain).toList();
    }

    @Override
    public void sync(String corpid, Long customerId, List<CustomerContact> contacts) {
        List<Long> retainedIds = contacts.stream().map(CustomerContact::getId).filter(id -> id != null).toList();
        if (retainedIds.isEmpty()) customerContactMapper.removeAll(corpid, customerId);
        else customerContactMapper.removeMissing(corpid, customerId, retainedIds);
        for (CustomerContact contact : contacts) {
            CustomerContactPO po = CustomerContactConvertor.toPO(contact);
            if (po.getId() == null) {
                long now = System.currentTimeMillis();
                po.setBizStatus("ENABLED");
                po.setDel(0);
                po.setAddTime(now);
                po.setUpdateTime(now);
                customerContactMapper.insert(po);
                contact.setId(po.getId());
            } else {
                po.setUpdateTime(System.currentTimeMillis());
                customerContactMapper.update(po);
            }
        }
    }
}
