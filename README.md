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
  * Dispatcher 1 carga todos los WorkItems con su Customer ID en la cola. Recoge los datos de Acme.
  * Dispatcher 2 carga todas las cuentas activas para cada cliente asociado a cada WorkItem en la cola. Recoge los datos de Acme System 3. 
* Performer se encarga de volcar los datos de la cola en la Web todas las cuentas activas asociadas a cada WorkItem.

![TO BE](https://github.com/hecikmc/RPA_gestion_workitems_Acme/assets/121127625/5735e66a-fe66-4c59-a26d-dbb7fd0b46cb)



