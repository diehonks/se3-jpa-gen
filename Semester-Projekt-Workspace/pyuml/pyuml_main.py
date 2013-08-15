import shutil
import os

OUTPUT_FOLDER = 'src_gen'
UML_FILE = "../semproj-generator/model/model.uml"
TEMPLATES = 'bottle_templates'

def clear_folder(folder):
    import os 
    for the_file in os.listdir(folder):
        file_path = os.path.join(folder, the_file)
        try:
            if os.path.isfile(file_path):
                os.unlink(file_path)
            else:
                shutil.rmtree(file_path)
        except OSError as e:
            print(e)

print('cleaning up')
clear_folder(OUTPUT_FOLDER)

print('creating UML tree')
import pyuml
import pyumljava
tree = pyuml.UMLParser(UML_FILE)
umljava = pyumljava.PyUMLJava(tree)

print('creating package structure and')
print('creating files from templates')
from bottle import template
for packagename, pkgcontent in umljava.packages.items():
    pkgdir = os.path.join(OUTPUT_FOLDER, packagename)
    os.makedirs(pkgdir)
    everything_in_package = pkgcontent['classes']+pkgcontent['interfaces']+pkgcontent['enums']
    for e in everything_in_package:
        rendered = template('java', template_lookup=['templates'], e=e)
        filename = e.name+'.java'
        with open(os.path.join(pkgdir, filename), 'w') as javafile:
            javafile.write(rendered)

