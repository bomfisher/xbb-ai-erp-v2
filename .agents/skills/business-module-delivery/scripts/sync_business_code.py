#!/usr/bin/env python3
"""校验并按显式开关登记字段元数据声明的业务编码。"""

import argparse
import re
from pathlib import Path

from validate_field_metadata import load_metadata, validate


def sync(metadata_path: Path, enum_path: Path, apply: bool) -> bool:
    metadata = load_metadata(metadata_path)
    errors = validate(metadata)
    if errors:
        raise ValueError("字段元数据校验失败，不能登记业务编码：\n- " + "\n- ".join(errors))
    business_code = metadata["businessCode"]
    source = enum_path.read_text(encoding="utf-8")
    if re.search(rf'\b{re.escape(business_code)}\s*\(\s*"{re.escape(business_code)}"\s*\)', source):
        print(f"BusinessCodeEnum 已登记：{business_code}")
        return False
    if not apply:
        raise ValueError(f"BusinessCodeEnum 缺少业务编码：{business_code}；确认后使用 --apply 登记")
    marker = "    ;\n"
    if marker not in source:
        raise ValueError(f"无法定位 BusinessCodeEnum 枚举结束标记：{enum_path}")
    enum_path.write_text(source.replace(marker, f'    {business_code}("{business_code}"),\n{marker}', 1), encoding="utf-8")
    print(f"BusinessCodeEnum 已登记：{business_code}")
    return True


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("field_metadata", type=Path)
    parser.add_argument("--enum-path", type=Path, required=True)
    parser.add_argument("--apply", action="store_true")
    args = parser.parse_args()
    sync(args.field_metadata, args.enum_path, args.apply)


if __name__ == "__main__":
    main()
