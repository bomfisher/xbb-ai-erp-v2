package xbb.ai.erp.module.masterdata.application.service.query;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import xbb.ai.erp.base.bizno.BizNoGenerator;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.module.masterdata.application.field.WarehouseFieldFactory;
import xbb.ai.erp.module.masterdata.domain.repository.WarehouseRepository;

class WarehouseAddItemTest {

    @Test
    void should_generate_warehouse_code_for_new_item() {
        BizNoGenerator generator = Mockito.mock(BizNoGenerator.class);
        Mockito.when(generator.next("corp-a", "WAREHOUSE")).thenReturn("WH-00001");
        WarehouseFieldFactory fieldFactory = Mockito.mock(WarehouseFieldFactory.class);
        Mockito.when(fieldFactory.getFields(Mockito.any())).thenReturn(List.of());
        WarehouseQueryAppServiceImpl service = new WarehouseQueryAppServiceImpl(
            Mockito.mock(WarehouseRepository.class), fieldFactory, null, null, generator);
        BaseDTO dto = new BaseDTO();
        dto.setCorpid("corp-a");

        assertEquals("WH-00001", service.addItem(dto).getData().getMain().getWarehouseCode());
        Mockito.verify(generator).next("corp-a", "WAREHOUSE");
    }
}
