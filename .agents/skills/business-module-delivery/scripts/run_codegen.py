#!/usr/bin/env python3
"""先 dry-run，再按显式开关执行通用业务模块代码生成。"""

import argparse
import subprocess
from pathlib import Path


def module_code(spec: Path) -> str:
    for line in spec.read_text(encoding="utf-8").splitlines():
        if line.startswith("moduleCode:"):
            return line.split(":", 1)[1].strip()
    raise ValueError(f"{spec} 缺少 moduleCode")


def run(project_root: Path, command: str, spec: Path) -> None:
    resolved_project_root = project_root.resolve()
    module_root = resolved_project_root / f"xbb-erp-module-{module_code(spec)}"
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
    module_codes = {module_code(spec) for spec in specs}
    for code in module_codes:
        module_root = project_root.resolve() / f"xbb-erp-module-{code}"
        subprocess.run(
            ["python3", str(Path(__file__).with_name("verify_mapper_registration.py")), str(module_root)],
            cwd=project_root.resolve(),
            check=True,
        )


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("--project-root", type=Path, required=True)
    parser.add_argument("--apply", action="store_true")
    parser.add_argument("specs", nargs="+", type=Path)
    args = parser.parse_args()
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
        verify_mapper_registration(args.project_root, args.specs)


if __name__ == "__main__":
    main()
