# RPA_gestion_workitems_Acme
Este proyecto consiste en un proceso automatizado 🤖 (boot) para verificar cuentas activas e inactivas para una serie de WorkItems.
Proyecto desarrollado en **Java**☕ con la herramienta **Workfusion**🔨.

# Descripción 
El proyecto consta de 3 procesos independientes (boots) que se encargan de:
* Realizar la verificación de cuentas activas e inactivas para una serie de WorkItems.
* Utiliza las plataformas ACME System 1 y ACME System 3 para obtener los datos necesarios.
* Cada WorkItem se encuentra registrado en la Web [Acme System 1](https://acme-test.uipath.com/home) en estado 'Open' y cuya descripcion es 'Verify account position'.
* Cada WorkItem esta vinculado a un cliente en concreto que hay que consultar en la aplicacion Acme System 3 (ejecutable adjunto al proyecto).
* Una vez leidas las cuentas activas vinculadas a cada cliente, se reporta la informacion en la Web.



