# Desarrollo de prueba de programación 

En este proyecto están las funcionalidades para gestionar clientes y productos. Se puede:
* Consultar clientes y productos
* Insertar productos
* Consultar posibles productos relacionados (de dos formas, por porcentaje de similitud de producto o por número de palabras)
* Asociar productos de diferentes clientes
* Comprobar productos asociados a un cliente

## Antes de comenzar

Se debe ejecutar el siguiente script en tu cliente de MySQL 8.0.25 (las propiedades de los datos de conexión están ubicadas en el fichero application.properties)

```
#creamos db
create database if not exists minderest_db;

use minderest_db;

CREATE TABLE CLIENT
	(id int NOT NULL AUTO_INCREMENT,
	 name VARCHAR(100) not null unique,
      PRIMARY KEY (id));

CREATE INDEX IDX_CLIENT ON CLIENT(name);



CREATE TABLE PRODUCT
	(id int NOT NULL AUTO_INCREMENT,
	 name VARCHAR(100) ,
	 id_client int not null,
	PRIMARY KEY (id),
	CONSTRAINT UK_PROD_CLI UNIQUE (id,name),
	FOREIGN KEY (id_client) REFERENCES CLIENT(id));
	
CREATE INDEX IDX_PRODUCT ON PRODUCT(name);

create table relation_products
(id_prod1 int not null,
id_prod2 int not null,
CONSTRAINT UK_RELATION_PRODUCTS UNIQUE (id_prod1,id_prod2),
FOREIGN KEY (id_prod1) REFERENCES PRODUCT(id),
FOREIGN KEY (id_prod2) REFERENCES PRODUCT(id));
```

## Despliegue de aplicación

Debe importar el proyecto a su IDE y desplegarlo como aplicación Spring boot seleccionando como clase principal ExerciseApplication.java.

## Documentación de la Api
Se ha integrado Swagger como documentación de Api. Se puede ver los servicios disponibles junto con una descripcion en el siguiente enlace una vez esté la aplicación ejecutándose en local:
http://localhost:8080/swagger-ui.html
