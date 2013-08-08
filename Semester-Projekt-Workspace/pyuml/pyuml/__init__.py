from xml.dom import minidom
import xml.dom
import sys


# Primitive value dummy classes
class JavaBuildIn:
    def __init__(self, name, pkgname):
        self.name = name
        self.package = {'name': pkgname}

SIMPLE_TYPE_BY_UML_TYPE = {
    'pathmap://UML_LIBRARIES/UMLPrimitiveTypes.library.uml#Integer': JavaBuildIn('Integer','java.lang'),
    'pathmap://UML_LIBRARIES/UMLPrimitiveTypes.library.uml#String': JavaBuildIn('String','java.lang'),
}

LATE_RESOLV = []

class Node(object):
    INDENTATION = 2
    
    def __init__(self, xmlnode, xmiid_map):
        self.node = xmlnode;
        self.name = self._getattr('name')
        self.xmiid = self._getattr('xmi:id')
        xmiid_map[self.xmiid] = self
        self.mapping = {}
    
    def _getattr(self, name):
        attrval = self.node.attributes.get(name)
        if attrval:
            return attrval.nodeValue
        else:
            return False
    
    def _buildChild(self, node, xmiid_map):
        if node.nodeType != xml.dom.Node.ELEMENT_NODE:
            return
        if node.nodeName == 'packagedElement':
            xmitype = node.attributes.get('xmi:type').nodeValue
            if xmitype in self.mapping:
                return self.mapping[xmitype](node, xmiid_map)
            else:
                print('No mapping for child node %s with xmitype "%s" from parent %s' % (node.nodeName, xmitype, self.name))
        
        if node.nodeName in self.mapping:
            return self.mapping[node.nodeName](node, xmiid_map)
        else:
            print('No mapping for node with name: "%s" in %s' % (node.nodeName, type(self)))
    
    def parseChildren(self, xmiid_map):
        children = []
        for child in self.node.childNodes:
            newborn = self._buildChild(child, xmiid_map)
            if newborn is not None:
                children.append(newborn)
        return children
    
    def tree_print(self, msg, level):
        print('   '*level + str(msg))
    
    def pretty_print(self, level=0):
        self.tree_print('   '*level + 'No pretty Print function for element %s' % self, level)
        
class Package(Node):
    def __init__(self, xmlnode, xmiid_map):
        super().__init__(xmlnode, xmiid_map)
        self.mapping = {
            'uml:Class' : Clazz,
            'uml:AssociationClass' : AssociationClazz,
            'uml:Interface' : Interface,
            'uml:Enumeration' : Enumeration,
            'uml:Association' : Association,
        }
        self.classes = []
        self.interfaces = []
        self.enums = []
        for child in self.parseChildren(xmiid_map):
            childtype = type(child)
            child.package = self
            if childtype == Enumeration:
                self.enums.append(child)
                continue
            elif childtype == Clazz:
                self.classes.append(child)
                continue
            elif childtype == Interface:
                self.interfaces.append(child)
                continue
        
    
    def pretty_print(self, level=0):
        self.tree_print('Package: %s' % self.name, level)
        for cls in self.classes:
            if cls is not None:
                cls.pretty_print(level + self.INDENTATION)

class Association(Node):
    def __init__(self, node, xmiid_map):
        super().__init__(node, xmiid_map)
        for owner in get_child_with_name(node, 'ownedEnd'):
            owner.attributes.get('type').nodeValue

class Clazz(Node):
    def __init__(self, node, xmiid_map):
        super().__init__(node, xmiid_map)
        self.mapping = {
            'ownedAttribute' : Member,
            'ownedOperation' : Operation,
            'generalization' : self.generalize,
            'ownedEnd': Member,
        }
        self.abstract = self._getattr('isAbstract')
        self.members = []
        self.operations = []
        self.inherits_from = []
        self.implements = []
        
        for child in self.parseChildren(xmiid_map):
            childtype = type(child)
            if childtype == Member:
                self.members.append(child)
            if childtype == Operation:
                self.operations.append(child)
    
    def generalize(self, node, xmiid_map):
        xmiid = node.attributes.get('general').nodeValue
        LATE_RESOLV.append((self.add_generalization, [xmiid]))
        
    def add_generalization(self, general):
        generalization_type = type(general)
        if generalization_type == Clazz:
            self.inherits_from.append(general)
        elif generalization_type == Interface:
            self.implements.append(general)
        else:
            print('unknown generalization of type "%s"'%generalization_type)
    
    def pretty_print(self, level=0):
        self.tree_print('Class: %s' % self.name, level)
        self.tree_print('|- is abstract: %s' % self.abstract, level)
        for member in self.members:
            member.pretty_print(level + self.INDENTATION)

class AssociationClazz(Clazz):
    def __init__(self, node, xmiid_map):
        super().__init__(node, xmiid_map)

class Interface(Clazz): #i'm lazy.
    def __init__(self, node, xmiid_map):
        super().__init__(node, xmiid_map)

class Enumeration(Node): #i'm lazy.
    def __init__(self, node, xmiid_map):
        super().__init__(node, xmiid_map)
        self.mapping = {
            'ownedLiteral' : Literal,
        }
        self.literals = [child for child in self.parseChildren(xmiid_map)]

class Literal(Node):
     def __init__(self, node, xmiid_map):
        super().__init__(node, xmiid_map)
        self.name = node.attributes.get('name').nodeValue

class Member(Node):
    
    DEFAULT_VISIBILITY = 'public'
    
    def __init__(self, node, xmiid_map):
        super().__init__(node, xmiid_map)
        self.visibility = self._getattr('visibility')
        if not self.visibility:
            self.visibility = self.DEFAULT_VISIBILITY
        self.type = None
        for typenode in get_child_with_name(self.node, 'type'):
            umltypename = typenode.attributes.get('href').nodeValue
            self.type = SIMPLE_TYPE_BY_UML_TYPE.get(umltypename)
            if self.type is None:
                print('missing type conversion for %s' % umltypename)
        if self.type is None:
            LATE_RESOLV.append((self.setType, [self.node.attributes.get('type').nodeValue]))
    
    def setType(self, typeref):
        self.type = typeref
       
    def pretty_print(self, level=0):
        self.tree_print('Member: %s' % self.name, level)
        self.tree_print('|- type: %s' % self.type, level)
        self.tree_print('|- visibility: %s' % self.visibility, level)

class Operation(Node):
    def __init__(self, node, xmiid_map):
        super().__init__(node, xmiid_map)
        for param in get_child_with_name(self.node, 'ownedParameter'):
            direction = param.attributes.get('direction').nodeValue
            if direction == 'return':
                for paramtype in get_child_with_name(param, 'type'):
                    returntypename = paramtype.attributes.get('href').nodeValue
                    self.returntype = SIMPLE_TYPE_BY_UML_TYPE.get(returntypename, 'unknown')
                    if self.returntype == 'unknown':
                        print('unknown returntype "%s"'%returntypename)
            else:
                print('unknown direction for Operation parameter "%s"'%direction)




def get_child_with_name(node, name):
    for child in node.childNodes:
        if child.nodeName == name:
            yield child

def parse_uml(umlfile):
    xmiid_map = {}
    doc = minidom.parse(umlfile)
    packages = []
    for node in doc.documentElement.childNodes:
        if node.nodeType == xml.dom.Node.ELEMENT_NODE:
            nodetype = node.attributes.get('xmi:type')
            # Package is topmost element,
            # all children are parsed by their parents
            if nodetype and nodetype.nodeValue == 'uml:Package':
                packages.append(Package(node, xmiid_map))
    # inheritance resolution
    for func, args in LATE_RESOLV:
        resolveargs = [xmiid_map[arg] for arg in args]
        func(*resolveargs)
    
    return packages

#packages = parse_uml("../../semproj-generator/model/model.uml")
#for pkg in packages:
#    pass
#    #pkg.pretty_print()