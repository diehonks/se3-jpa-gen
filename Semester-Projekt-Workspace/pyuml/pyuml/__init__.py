from xml.dom import minidom
import xml.dom

class UMLNode(object):
    def __init__(self, parent, xmlnode, xmiid_map):
        self.parent = parent
        if 'xmi:id' in xmlnode.attributes:
            xmiid_map[xmlnode.attributes.get('xmi:id').nodeValue] = self
        self.name = xmlnode.tagName
        self.children = []
        self.profiles = []
        self.refs = {}
        self.attrs = {n: v for n, v in xmlnode.attributes.items()}
        for child in xmlnode.childNodes:
            if child.nodeType == xml.dom.Node.ELEMENT_NODE:
                self.children.append(UMLNode(self, child, xmiid_map))
    
    def getChildByTagName(self, tagname):
        return [child for child in self.children if child.name == tagname]
    
    def getChildByAttr(self, attrname, attrvalue):
        return [c for c in self.children if attrvalue == c.attrs.get(attrname)]
    
    def getChildByRef(self, refname, refvalue):
        return [c for c in self.children if refvalue in c.attrs.get(refname, [])]
    
    def __repr__(self):
        name = self.name
        if 'xmi:type' in self.attrs:
            name += ' '+self.attrs['xmi:type']
        if 'name' in self.attrs:
            name += ' '+self.attrs['name']
        return name

class UMLParser:
    XMI_ID_REFS = {
        'generalization': ['general'],
        'ownedAttribute': ['type', 'association'],
        'ownedEnd': ['type', 'association'],
        'packagedElement': ['navigableOwnedEnd'],
    }
    
    def __init__(self, umlfile):
        self.xmiid_map = {}
        self.umlrepr = []
        doc = minidom.parse(umlfile)
        self.packages = []
        self.profiles = []
        model = doc.documentElement.getElementsByTagName('uml:Model')[0]
        for node in model.childNodes:
            if node.nodeType == xml.dom.Node.ELEMENT_NODE:
                nodetype = node.attributes.get('xmi:type')
                # Package is topmost element,
                # all children are parsed by their parents
                if nodetype and nodetype.nodeValue == 'uml:Package':
                    self.packages.append(UMLNode(None, node, self.xmiid_map))
        
        #add profile information:
        for node in doc.documentElement.childNodes:
            if node.nodeType == xml.dom.Node.ELEMENT_NODE:
                if node.tagName.startswith('Profile'):
                    for name, value in node.attributes.items():
                        if name.startswith('base_'):
                            node_with_profile = self.xmiid_map[value]
                            node_with_profile.profiles.append(node.tagName)

        #resolve xmiid_map
        for xmlnode in self.xmiid_map.values():
            for attr_resolv in self.XMI_ID_REFS.get(xmlnode.name, []):
                if attr_resolv in xmlnode.attrs:
                    xmiids = xmlnode.attrs[attr_resolv].split(' ')
                    xmlnode.attrs[attr_resolv] = [self.xmiid_map[xmiid] for xmiid in xmiids]
                    xmlnode.refs[attr_resolv] = [self.xmiid_map[xmiid] for xmiid in xmiids]

    def getRoot(self):
        return self.packages
                        
    def getElementByAttr(self, attrname, attrvalue):
        elems = []
        for e in self.xmiid_map.values():
            if e.attrs.get(attrname) == attrvalue:
                elems.append(e)
        return elems
    
    def getElementByTagName(self, tagname):
        return [e for e in self.xmiid_map.values() if e.name == tagname]