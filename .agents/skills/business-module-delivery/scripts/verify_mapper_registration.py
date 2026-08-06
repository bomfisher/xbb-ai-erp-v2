#!/usr/bin/env python3
"""校验模块 Mapper 可被管理端的 @MapperScan 注册。"""

import argparse
from pathlib import Path


MAPPER_IMPORT = "import org.apache.ibatis.annotations.Mapper;"
MAPPER_ANNOTATION = "@Mapper"


def validate(module_root: Path) -> None:
    mapper_dir = module_root / "src/main/java"
    mapper_files = sorted(mapper_dir.glob("**/infrastructure/persistence/mapper/*Mapper.java"))
    if not mapper_files:
        raise ValueError(f"未找到 Mapper 文件：{module_root}")

    invalid_files = [
        mapper_file
        for mapper_file in mapper_files
        if MAPPER_IMPORT not in mapper_file.read_text(encoding="utf-8")
        or MAPPER_ANNOTATION not in mapper_file.read_text(encoding="utf-8")
    ]
    if invalid_files:
        details = "、".join(str(mapper_file) for mapper_file in invalid_files)
        raise ValueError(f"Mapper 缺少 @Mapper 注册注解：{details}")

    print(f"Mapper 注册校验通过：{module_root}（{len(mapper_files)} 个）")


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("module_root", type=Path)
    args = parser.parse_args()
    validate(args.module_root)


if __name__ == "__main__":
    main()
