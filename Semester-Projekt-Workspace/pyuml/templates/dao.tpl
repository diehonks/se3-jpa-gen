package {{daopackage}};

import java.util.List;

import {{cls.package}}.{{cls.name}};

public class {{cls.name}}DAO extends ADAO {

	public {{cls.name}} create{{cls.name}}(final {{cls.name}} {{cls.name.lower()}}) {
		return create({{cls.name.lower()}});
	}

	public {{cls.name}} read{{cls.name}}(final long id) {
		return read({{cls.name}}.class, id);
	}

	public {{cls.name}} update{{cls.name}}(final {{cls.name}} {{cls.name.lower()}}) {
		return update({{cls.name.lower()}});
	}

	public List<{{cls.name}}> readAll{{cls.name}}s() {
		return readByJPQL("select t from {{cls.name}} t");
	}

	public void delete{{cls.name}}(final {{cls.name}} {{cls.name.lower()}}) {
		delete({{cls.name.lower()}});
	}
    
    public void deleteAll{{cls.name}}s() {
		deleteAll({{cls.name}}.class);
	}

}
