class JPrimitive(object):
    def __init__(self, name, package):
        self.name = name
        self.package = package

class PyUMLJava(object):
    PRIMITIVES = {
        'pathmap://UML_LIBRARIES/UMLPrimitiveTypes.library.uml#Integer':
            JPrimitive('Long', 'java.lang'),
        'pathmap://UML_LIBRARIES/UMLPrimitiveTypes.library.uml#String':
            JPrimitive('String', 'java.lang'),
        'pathmap://UML_LIBRARIES/UMLPrimitiveTypes.library.uml#Object':
            JPrimitive('Object', 'java.lang'),
        'pathmap://UML_LIBRARIES/UMLPrimitiveTypes.library.uml#Real':
            JPrimitive('Double', 'java.lang'),
    }
    
    def __init__(self, pyUML):
        rootpackages = pyUML.getRoot()
        self.packages = {}
        self.classes = []
        self.enums = []
        self.interfaces = []
        for package in rootpackages:
            self.packages[package.attrs['name']] = {
                'classes': [JClass(e) for e in package.getChildByAttr('xmi:type', 'uml:Class')],
                'enums': [JEnum(e) for e in package.getChildByAttr('xmi:type', 'uml:Enumeration')],
                'interfaces': [JInterface(e) for e in package.getChildByAttr('xmi:type', 'uml:Interface')],
            }
    
    def resolve_type(umlnode):
        if 'type' in umlnode.attrs:
            # custom type
            return JClass(umlnode.attrs['type'])
        else:
            # primitive type
            primitive = umlnode.getChildByTagName('type')[0]
            return PyUMLJava.PRIMITIVES[primitive.attrs['href']]
    
    def __repr__(self):
        return '''classes: %s

enums: %s

interfaces: %s
''' % (str(self.classes), str(self.enums), str(self.interfaces))

class JMember(object):
    DEFAULT_VISIBLITY = ''

    def __init__(self, umlnode):
        self.umlnode = umlnode
        self.name = umlnode.attrs['name']
        self.visibility = umlnode.attrs.get('visibility', self.DEFAULT_VISIBLITY)
        self.type = PyUMLJava.resolve_type(self.umlnode)
        
    
    def __repr__(self):
        return '''%s %s %s
        ''' % (self.visibility, self.type, self.name)

class JOperation(object):
    DEFAULT_VISIBLITY = 'public'
    def __init__(self, umlnode):
        self.umlnode = umlnode
        self.name = umlnode.attrs['name']
        self.constructor = self.name == umlnode.parent.attrs['name']
        self.abstract = umlnode.attrs.get('abstract', False) 
        self.visibility = umlnode.attrs.get('visibility', self.DEFAULT_VISIBLITY)
        try:
            returnwrapper = self.umlnode.getChildByAttr('direction', 'return')[0]
            self.returns = PyUMLJava.resolve_type(returnwrapper)
        except IndexError:
            self.returns = None
    
    def __repr__(self):
        return '''%s %s %s
        ''' % (self.visibility, self.returns, self.name)
        
class JEnum(object):
    def __init__(self, umlnode):
        self.umlnode = umlnode
        self.package = umlnode.parent.attrs['name']
        self.name = umlnode.attrs['name']
        self.literals = [l.attrs['name'].upper() for l in self.umlnode.getChildByTagName('ownedLiteral')]
    
    def __repr__(self):
        return self.name + str(self.literals)

class JClass(object):
    def __init__(self, umlnode):
        self.umlnode = umlnode
        self.name = umlnode.attrs['name']
        self.package = umlnode.parent.attrs['name']
        self.members = [JMember(e) for e in umlnode.getChildByTagName('ownedAttribute')]
        self.operations = [JOperation(e) for e in umlnode.getChildByTagName('ownedOperation')]
    
    def __repr__(self):
        return """
        %s.%s
        
        //members
        %s
        
        //operations
        %s
        """ % (self.package, self.name, self.members, self.operations)

class JInterface(JClass):
    def __init__(self, umlnode):
        super().__init__(umlnode)