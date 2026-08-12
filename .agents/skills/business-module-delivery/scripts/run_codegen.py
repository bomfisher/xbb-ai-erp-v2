#!/usr/bin/env python3
"""先 dry-run，再按显式开关执行通用业务模块代码生成。"""

import argparse
import re
import subprocess
from pathlib import Path


def property_value(spec: Path, property_name: str) -> str:
    for line in spec.read_text(encoding="utf-8").splitlines():
        if line.startswith(f"{property_name}:"):
            return line.split(":", 1)[1].strip()
    raise ValueError(f"{spec} 缺少 {property_name}")


def module_code(spec: Path) -> str:
    return property_value(spec, "moduleCode")


def module_dir(spec: Path) -> str:
    value = property_value(spec, "moduleDir")
    if not re.fullmatch(r"[a-z0-9]+(?:-[a-z0-9]+)*", value):
        raise ValueError(f"{spec} 的 moduleDir 必须只使用小写字母、数字和短横线：{value}")
    return value


def run(project_root: Path, command: str, spec: Path) -> None:
    resolved_project_root = project_root.resolve()
    module_root = resolved_project_root / f"xbb-erp-module-{module_dir(spec)}"
    if not module_root.is_dir():
        raise ValueError(f"目标模块不存在：{module_root}")
    arguments = f"{command} {spec.resolve()} {module_root}"
    subprocess.run(
        [
            "mvn", "-f", str(resolved_project_root / "xbb-erp-codegen" / "pom.xml"), "exec:java",
            "-Dexec.mainClass=xbb.ai.erp.codegen.cli.CodegenCli",
            f"-Dexec.args={arguments}",
        ],
        cwd=resolved_project_root,
        check=True,
    )


def verify_mapper_registration(project_root: Path, specs: list[Path]) -> None:
    module_dirs = {module_dir(spec) for spec in specs}
    for directory_name in module_dirs:
        module_root = project_root.resolve() / f"xbb-erp-module-{directory_name}"
        subprocess.run(
            ["python3", str(Path(__file__).with_name("verify_mapper_registration.py")), str(module_root)],
            cwd=project_root.resolve(),
            check=True,
        )


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("--project-root", type=Path, required=True)
    parser.add_argument("--field-metadata", type=Path)
    parser.add_argument("--apply", action="store_true")
    parser.add_argument("specs", nargs="+", type=Path)
    args = parser.parse_args()
    if args.field_metadata and len(args.specs) != 1:
        raise ValueError("使用 --field-metadata 时一次只能生成一个 ROOT 规格")
    subprocess.run(
        ["python3", str(Path(__file__).with_name("validate_module_specs.py")), *map(str, args.specs)],
        cwd=args.project_root.resolve(),
        check=True,
    )
    for spec in args.specs:
        run(args.project_root, "dry-run", spec)
    if args.apply:
        for spec in args.specs:
            run(args.project_root, "generate", spec)
            if args.field_metadata:
                subprocess.run(
                    ["python3", str(Path(__file__).with_name("generate_list_meta_provider.py")),
                     str(args.field_metadata), str(spec), str(args.project_root.resolve() / f"xbb-erp-module-{module_dir(spec)}"), "--apply"],
                    cwd=args.project_root.resolve(), check=True,
                )
                subprocess.run(
                    ["python3", str(Path(__file__).with_name("generate_query_form_contract.py")),
                     str(args.field_metadata), str(spec), str(args.project_root.resolve() / f"xbb-erp-module-{module_dir(spec)}"), "--apply"],
                    cwd=args.project_root.resolve(), check=True,
                )
        verify_mapper_registration(args.project_root, args.specs)


if __name__ == "__main__":
    main()
