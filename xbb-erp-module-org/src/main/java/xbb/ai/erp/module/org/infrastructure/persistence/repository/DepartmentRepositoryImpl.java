package xbb.ai.erp.module.org.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.org.domain.model.Department;
import xbb.ai.erp.module.org.infrastructure.persistence.convertor.OrgConvertor;
import xbb.ai.erp.module.org.infrastructure.persistence.mapper.DepartmentMapper;
import xbb.ai.erp.module.org.infrastructure.persistence.po.DepartmentPO;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DepartmentRepositoryImpl {

    private final DepartmentMapper departmentMapper;

    public void insert(Department department) {
        DepartmentPO po = OrgConvertor.toPO(department);
        departmentMapper.insert(po);
        department.setId(po.getId());
    }

    public Department findById(String corpid, Long id) {
        return OrgConvertor.toDomain(departmentMapper.selectOne(new LambdaQueryWrapper<DepartmentPO>()
            .eq(DepartmentPO::getCorpid, corpid)
            .eq(DepartmentPO::getId, id)));
    }

    public List<Department> list(String corpid, Integer departmentStatus) {
        return departmentMapper.selectList(new LambdaQueryWrapper<DepartmentPO>()
                .eq(DepartmentPO::getCorpid, corpid)
                .eq(departmentStatus != null, DepartmentPO::getDepartmentStatus, departmentStatus)
                .orderByAsc(DepartmentPO::getDepartmentLevel, DepartmentPO::getId))
            .stream()
            .map(OrgConvertor::toDomain)
            .toList();
    }

    public List<Department> listByIds(String corpid, List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return departmentMapper.selectList(new LambdaQueryWrapper<DepartmentPO>()
                .eq(DepartmentPO::getCorpid, corpid)
                .in(DepartmentPO::getId, ids))
            .stream()
            .map(OrgConvertor::toDomain)
            .toList();
    }
}
