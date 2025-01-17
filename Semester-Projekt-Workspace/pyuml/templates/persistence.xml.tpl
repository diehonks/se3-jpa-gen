<?xml version="1.0" encoding="UTF-8" ?>
<persistence xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://java.sun.com/xml/ns/persistence http://java.sun.com/xml/ns/persistence/persistence_2_0.xsd"
  version="2.0" xmlns="http://java.sun.com/xml/ns/persistence">
  <persistence-unit name="test_inmem_db" transaction-type="RESOURCE_LOCAL">
    <!--<exclude-unlisted-classes>false</exclude-unlisted-classes>-->
%for cls in classes:
    <class>{{cls.package}}.{{cls.name}}</class>
%end
    <properties>
        <property name="eclipselink.target-database" value="org.eclipse.persistence.platform.database.H2Platform"/>        
        <property name="javax.persistence.jdbc.password" value="sa"/>
        <property name="javax.persistence.jdbc.user" value="sa"/>
        <property name="javax.persistence.jdbc.driver" value="org.h2.Driver"/>
        <property name="javax.persistence.jdbc.url" value="jdbc:h2:mem:test_inmem_db;DB_CLOSE_DELAY=-1"/>
        <!--<property name="eclipselink.jdbc.url" value="jdbc:h2:tcp://localhost/~/aulaweb/db/database"/>-->
        <property name="eclipselink.ddl-generation" value="drop-and-create-tables"/>
    </properties>
  </persistence-unit>
</persistence> 