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
    
    def __init__(self, pyUML, debug=False):
        self.debug = debug
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
        for package in rootpackages:
            self.associate(package.getChildByAttr('xmi:type', 'uml:Association'))
    
    def associate(self, assocs):
        for assoc in assocs:
            ownedEnds = assoc.getChildByTagName('ownedEnd')
            if len(ownedEnds) == 1:
                ownerclass = PyUMLJava.resolve_type(ownedEnds[0])
                ownedAttribute = ownedEnds[0].attrs['type'][0].getChildByRef('association', assoc)[0]
                assocclass = PyUMLJava.resolve_type(ownedAttribute)
                assocname = ownedAttribute.attrs['name']
                ownerclass.members.append(JMember(assocclass, assocname))
                if self.debug:
                    print('Simple Association %s owns %s named %s' % (ownerclass.name, assocclass.name, assocname))
            elif len(ownedEnds) == 2:
                first, second = ownedEnds
                first_cls = PyUMLJava.resolve_type(first)
                sec_cls = PyUMLJava.resolve_type(second)
                belongsTo = {
                    first : sec_cls,
                    second : first_cls,
                }
                upperValues = {
                    first: first.getChildByTagName('upperValue')[0].attrs['value'],
                    second: second.getChildByTagName('upperValue')[0].attrs['value']
                }
                for ownedEnd in assoc.attrs['navigableOwnedEnd']:
                    member_for_cls = belongsTo[ownedEnd]               
                    assocname = assoc.attrs['name']+ownedEnd.attrs['name'][0].upper()+ownedEnd.attrs['name'][1:]
                    assoctype = PyUMLJava.resolve_type(ownedEnd)
                    upper = ownedEnd.getChildByTagName('upperValue')[0].attrs['value']
                    lower = ownedEnd.getChildByTagName('lowerValue')[0].attrs.get('value', '0')
                    if self.debug:
                        print('complex assoc %s owns %s named %s [%s-%s]'%
                              (member_for_cls.name, assoctype.name, assocname, lower, upper))
                    multiplicity = '@'
                    if ownedEnd == first:
                        multiplicity += 'Many' if upperValues[second] == '*' else 'One'
                    else:
                        multiplicity += 'Many' if upperValues[first] == '*' else 'One'
                    multiplicity += 'To'
                    if ownedEnd == first:
                        multiplicity += 'Many' if upperValues[first] == '*' else 'One'
                    else:
                        multiplicity += 'Many' if upperValues[second] == '*' else 'One'
                    member_for_cls.members.append(JMember(assoctype, assocname, lower, upper, multiplicity=multiplicity))

    
    def resolve_type(umlnode):
        if 'type' in umlnode.attrs:
            # custom type
            umlclass = umlnode.attrs['type'][0]
            classname = umlclass.attrs['name']
            obj = JClass.byName(classname)
            if obj is not None:
                return obj
            obj = JEnum.byName(classname)
            if obj is not None:
                return obj
            raise Exception('Class for Association not Found! %s Creating one on the fly.' % umlclass)
        else:
            # primitive type
            try:
                primitive = umlnode.getChildByTagName('type')[0]
            except IndexError:
                print('Error resolving type:'+ str(umlnode))
                return None
            return PyUMLJava.PRIMITIVES[primitive.attrs['href']]
    
    def __repr__(self):
        retstr = ''
        for pkgname, pkgcontent in self.packages.items():
            retstr += '''PACKAGE: %s
            classes: %s

enums: %s

interfaces: %s
''' %  (pkgname,
        str(pkgcontent['classes']),
        str(pkgcontent['enums']),
        str(pkgcontent['interfaces']),
        )
        return retstr

class JMember(object):
    DEFAULT_VISIBLITY = 'public'
    DEFAULT_LOWER = '0'
    DEFAULT_UPPER = '1'
    def __init__(self, _type, name, lower=DEFAULT_LOWER, upper=DEFAULT_UPPER, visibility=DEFAULT_VISIBLITY, multiplicity=''):
        self.name = name
        self.upper = upper
        self.lower = lower
        self.multiplicity = multiplicity
        self.visibility = visibility
        self.type = _type

class JMemberPrimitive(JMember):
    def __init__(self, umlnode):
        super().__init__(
            PyUMLJava.resolve_type(umlnode),
            umlnode.attrs['name'],
            umlnode.attrs.get('visibility', self.DEFAULT_VISIBLITY),
        )
        self.umlnode = umlnode
        lowernode = umlnode.getChildByTagName('lowerValue')
        if len(lowernode) > 0:
            self.lower = lowernode[0].attrs.get('value', self.DEFAULT_LOWER)
            self.upper = umlnode.getChildByTagName('upperValue')[0].attrs['value']
        
    
    def __repr__(self):
        return '''%s %s %s
        ''' % (self.visibility, self.type, self.name)

class JOperation(object):
    DEFAULT_VISIBLITY = 'public'
    def __init__(self, umlnode, cls):
        self.umlnode = umlnode
        self.name = umlnode.attrs['name']
        self.constructor = self.name == umlnode.parent.attrs['name']
        self.abstract = umlnode.attrs.get('abstract', False)
        self.visibility = umlnode.attrs.get('visibility', [self.DEFAULT_VISIBLITY])
        try:
            returnwrapper = self.umlnode.getChildByAttr('direction', 'return')[0]
            self.returns = PyUMLJava.resolve_type(returnwrapper)
            if self.returns is None:
                print('return type has no value, assuming return of same class')
                self.returns = cls
        except IndexError:
            self.returns = None
    
    def __repr__(self):
        return '''%s %s %s
        ''' % (self.visibility, self.returns, self.name)
        
class JEnum(object):
    ENUMS = []
    
    def byName(name):
        for e in JEnum.ENUMS:
            if e.name == name:
                return e
    
    def __init__(self, umlnode):
        self.ENUMS.append(self)
        self.umlnode = umlnode
        self.package = umlnode.parent.attrs['name']
        self.name = umlnode.attrs['name']
        self.literals = [l.attrs['name'].upper() for l in self.umlnode.getChildByTagName('ownedLiteral')]
    
    def __repr__(self):
        return self.name + str(self.literals)

class JClass(object):
    
    CLASSES = []
    
    def byName(name):
        for c in JClass.CLASSES:
            if c.name == name:
                return c
    
    def __init__(self, umlnode):
        self.CLASSES.append(self)
        self.umlnode = umlnode
        self.name = umlnode.attrs['name']
        self.package = umlnode.parent.attrs['name']
        self.abstract = umlnode.attrs.get('abstract', 'false') == 'true'
        self.members = []
        for e in umlnode.getChildByTagName('ownedAttribute'):
            if not 'association' in e.attrs and len(e.getChildByTagName('type')) > 0:
                self.members.append(JMemberPrimitive(e))
        self.operations = [JOperation(e, self) for e in umlnode.getChildByTagName('ownedOperation')]
        self.inherits_from = None
        self.implements = []
        for general in umlnode.getChildByTagName('generalization'):
            refgenereal = general.attrs['general'][0]
            refgentype = refgenereal.attrs.get('xmi:type')
            if refgentype == 'uml:Class':
                if self.inherits_from is None:
                    self.inherits_from = JClass(refgenereal)
                    # print("%s inherits from %s " % (self.name, self.inherits_from.name))
                else:
                    print("Java doesn't support multiple inheritance! %s"%
                          (umlnode))
            elif refgentype == 'uml:Interface':
                self.implements = JInterface(refgenereal)
            else:
                print('unknown generalization: %s for %s' % (refgentype, umlnode))
    
    def members_rec(self):
        """returns all members and the members of any super classes"""
        all_members_but_id = [m for m in self.members if m.name != 'id']
        if not self.inherits_from is None:
            return all_members_but_id + self.inherits_from.members_rec()
        return all_members_but_id

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