
from glob import glob
from pathlib import Path
from pathlib import PurePath
from configparser import ConfigParser

IGNORE = [
    ".git",
    ".idea",
    ".gradle",
    "gradle",
    "gradlew",
    "*.bat",
    "template.py"
]

ROOT = Path(__file__).parent
CONFIG = ConfigParser()

TEMPLATE = {
    "mod_id"    : "",
    "mod_name"  : "",
    "mod_class" : "",
    "mod_group" : "",
}

def main():
    read_gitignore()
    parse_properties()

    files = []
    dirs  = []

    for file in glob("**/*", recursive=True, root_dir=ROOT):
        if is_ignored(file):
            continue

        path = Path(file)

        if should_apply(path):
            if path.is_file():
                files.append(path)
            elif path.is_dir():
                dirs.apend(path)

    apply_files(files)
    apply_dirs(dirs)

def apply_files(files: list[Path]):
    for path in files:
        path.write_text(apply_file(path), encoding="utf-8")
        path.move(apply_path(path))

def apply_dirs(dirs: list[Path]):
    for path in dirs:
        path.move(apply_path(path))

def apply_file(path: Path):
    res = path.read_text(encoding="utf-8")

    for key in TEMPLATE:
        res = res.replace(keyof(key), TEMPLATE[key])

    return res

def apply_path(path: Path):
    result = str(path)

    for k in TEMPLATE:
        result = result.replace(keyof(k), TEMPLATE[k])

    return Path(result)

def should_apply(path: Path):
    strpath = str(path)
    contents = path.read_text(encoding="utf-8")

    for key in TEMPLATE:
        if keyof(key) in strpath:
            return True
        elif keyof(key) in contents:
            return True

    return False

def keyof(key):
    return "{{" + key + "}}"

def is_ignored(path):
    pure = PurePath(path)

    for ignored in IGNORE:
        if path.startswith(ignored) or pure.full_match(ignored):
            return True

    return False

def read_gitignore():
    if Path(".gitignore").exists():
        for line in Path(".gitignore").read_text().splitlines():
            if line.strip() != "" and not line.startswith("#"):
                IGNORE.append(line.strip())

def parse_properties():
    CONFIG.read_string("[template]\n" + Path("gradle.properties").read_text())
    TEMPLATE["mod_id"] = CONFIG.get("template", "mod_id")
    TEMPLATE["mod_name"] = CONFIG.get("template", "mod_name")
    TEMPLATE["mod_class"] = CONFIG.get("template", "mod_class")
    TEMPLATE["mod_group"] = TEMPLATE["mod_class"].lower()

if __name__ == "__main__":
    main()
