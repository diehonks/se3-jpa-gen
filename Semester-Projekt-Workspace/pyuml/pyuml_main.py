import shutil
import os

OUTPUT_FOLDER = 'src_gen'
UML_FILE = "../../JPA-example-UML/model.uml"
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

#print('cleaning up')
#clear_folder(OUTPUT_FOLDER)

#print('creating UML tree')
import pyuml
import pyumljava
tree = pyuml.UMLParser(UML_FILE)
umljava = pyumljava.PyUMLJava(tree, debug=True)

def mkdir_ignore(dirname):
    try:
        os.makedirs(dirname)
    except OSError:
        pass #dir already exists

#print('creating package structure and')
#print('creating files from templates')
from bottle import template
for packagename, pkgcontent in umljava.packages.items():
    # create POJOS
    pkgdir = os.path.join(OUTPUT_FOLDER, packagename.replace('.','/'))
    mkdir_ignore(pkgdir)
    everything_in_package = pkgcontent['classes']+pkgcontent['interfaces']+pkgcontent['enums']
    for e in everything_in_package:
        rendered = template('java', template_lookup=['templates'], e=e)
        filename = e.name+'.java'
        with open(os.path.join(pkgdir, filename), 'w') as javafile:
            javafile.write(rendered)
    
    # create DAOS
    daopackage = '.'.join(packagename.split('.')[:-1])+'.daos'
    daopkgdir = os.path.join(OUTPUT_FOLDER, daopackage.replace('.','/'))
    mkdir_ignore(daopkgdir)
    for cls in pkgcontent['classes']:
        rendered = template('dao', template_lookup=['templates'], cls=cls, daopackage=daopackage)
        filename = cls.name+'DAO.java'
        with open(os.path.join(daopkgdir, filename), 'w') as daofile:
            daofile.write(rendered)

