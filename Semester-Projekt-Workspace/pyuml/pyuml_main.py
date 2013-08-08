import pyuml
#import jinja2
from jinja2 import Environment, PackageLoader
import os
import shutil

OUTPUT_FOLDER = 'src_gen'

packages = pyuml.parse_uml("../semproj-generator/model/model.uml")
env = Environment(loader=PackageLoader('templates', 'java'))

def generate_project(packages, package_hierarchy=[]):
    for pkg in packages:
        curr_pkg_hier = package_hierarchy+[pkg.name]
        curr_dir = os.sep.join([OUTPUT_FOLDER]+curr_pkg_hier)
        packagename = '.'.join(curr_pkg_hier)
        try:
            os.makedirs(curr_dir)
        except OSError:
            pass
        for clazz in pkg.classes:
            filename = clazz.name+'.java'
            absfilename = curr_dir+os.sep+filename
            template = env.get_template('class.tmpl')
            with open(absfilename, 'w') as fh:
                fh.write(template.render(data=clazz, pkg=packagename))
        for inter in pkg.interfaces:
            filename = inter.name+'.java'
            absfilename = curr_dir+os.sep+filename
            template = env.get_template('interface.tmpl')
            with open(absfilename, 'w') as fh:
                fh.write(template.render(data=inter, pkg=packagename))

generate_project(packages)