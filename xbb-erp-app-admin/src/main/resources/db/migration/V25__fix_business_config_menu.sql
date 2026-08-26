UPDATE sys_menu AS target_menu
LEFT JOIN sys_menu AS existing_menu
    ON existing_menu.corpid = target_menu.corpid
    AND existing_menu.menu_code = 'SYSTEM_DATA_DICTIONARY'
    AND existing_menu.del = 0
SET target_menu.menu_code = 'SYSTEM_DATA_DICTIONARY',
    target_menu.menu_name = '数据字典管理',
    target_menu.route_path = '/management/system/data-dictionary',
    target_menu.component_path = 'management/system/data-dictionary',
    target_menu.update_time = UNIX_TIMESTAMP(CURRENT_TIMESTAMP(3)) * 1000,
    target_menu.modify_id = 'system'
WHERE target_menu.menu_code = 'ATTACHMENT_TEMPLATE'
  AND target_menu.route_path = '/management/system/business-config'
  AND target_menu.component_path = 'management/system/business-config'
  AND existing_menu.id IS NULL;
