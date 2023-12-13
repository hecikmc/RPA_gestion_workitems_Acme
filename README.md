# RPA_gestion_workitems_Acme
Este proyecto consiste en un proceso automatizado 🤖 (boot) para verificar cuentas activas e inactivas para una serie de WorkItems.

Proyecto desarrollado en **Java** ☕ con la herramienta [Workfusion](https://doc.workfusion.com/enterprise/docs/iac/core/studio/install-iac-developer/) 🔨.

## Descripción 
El proyecto consta de 3 procesos independientes (boots: Dispatcher_1, Dispatcher_2, Performer) que se encargan de:
* Realizar la verificación de cuentas activas e inactivas para una serie de WorkItems.
* Utiliza las plataformas ACME System 1 y ACME System 3 para obtener los datos necesarios.
* Cada WorkItem se encuentra registrado en la Web [Acme System 1](https://acme-test.uipath.com/home) en estado 'Open' y cuya descripcion es 'Verify account position'.
* Cada WorkItem esta vinculado a un cliente en concreto que hay que consultar en la aplicacion Acme System 3 (ejecutable adjunto al proyecto).
* Una vez leidas las cuentas activas vinculadas a cada cliente, se reporta la informacion en la Web Acme System 1.


## Flujo de procesos

Flujo de actividades desarrolladas por cada proceso:
* Dispatcher 1 y Dispatcher 2 recogen los datos necesarios para cargarlos en la cola de items a procesar.
  * Dispatcher 1 carga todos los WorkItems con su Customer ID en la cola. Recoge los datos de la web Acme.
  * Dispatcher 2 carga todas las cuentas activas para cada cliente asociado a cada WorkItem en la cola. Recoge los datos de Acme System 3 (aplicacion). 
* Performer se encarga de volcar los datos de la cola en la Web todas las cuentas activas asociadas a cada WorkItem.

![TO BE](https://github.com/hecikmc/RPA_gestion_workitems_Acme/assets/121127625/5735e66a-fe66-4c59-a26d-dbb7fd0b46cb)

## Requisitos de ejecucion
Para poder ejecutar el proyecto, es necesario disponer de:
* Workfusion (con licencia). Puedes decargarlo aqui: https://doc.workfusion.com/enterprise/docs/iac/core/studio/install-iac-developer/
 
  * En la herramienta Studio (eclipse) de WorkFusion, hay que importar la estructura de carpetas con Maven:

  ![image](https://github.com/hecikmc/RPA_gestion_workitems_Acme/assets/121127625/70eb993b-66c9-4259-9d66-236162ce29da)

* DBeaver para la gestion de la cola (mediante base de datos). Puedes decargarlo aqui: https://dbeaver.io/download/ 
* Usuario y contraseña para acceder a los sistemas de Acme (acceso gratuito). Para darse de alta hay que registrarse aqui: https://acme-test.uipath.com/register 

### WorFusion
Es necesario disponer de 2 variables 'SecretsVault' creadas en Studio:
* Una guarda las credenciales de acceso a la web y a la aplicacion (el alias debe ser 'loginAcces' y cuya clave es el usuario de acceso y el valor corresponde a la contraseña)
* La otra guarda las credenciales de acceso a la base de datos que gestiona la cola de items (el alias debe ser 'aliasbd').

![image](https://github.com/hecikmc/RPA_gestion_workitems_Acme/assets/121127625/36bcc3fb-fce1-4b1f-be09-bb9bc20fc2ce)

### Fichero ../utils/Constans.java
* Es necesario actualizar la URL de JDB con la URL de configuracion de tu servidor de DBeaber.
* Es necesario actualizar la ruta del ejecutable donde tengas guardado el fichero ACME-System3.exe

## Arquetipo utilizado

```shell
mvn archetype:generate -DarchetypeGroupId="com.workfusion.odf2" -DarchetypeArtifactId="simple-archetype" -DarchetypeVersion="10.2.6.10" -DgroupId="com.example" -DartifactId="example-project4" -Dversion="1.0" -Dpackage="com.example" -Dusecase-code="uc-code" -Dusecase-name="uc-name" -Dcontrol-tower-url="http://localhost:15280" -DinteractiveMode=false


