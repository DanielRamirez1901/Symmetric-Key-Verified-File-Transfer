# Informe del Programa

**Desarrollador:** Daniel Ramirez Gomez

## Desarrollo del Programa

El programa ha sido desarrollado en Java utilizando el entorno de desarrollo IntelliJ. Su objetivo principal es facilitar la transferencia segura de archivos mediante el intercambio de claves simétricas.

## Flujo del Programa

1. **Conexión por Sockets:**
   El programa inicia estableciendo una conexión por sockets entre el cliente y el servidor.

2. **Menú de Selección de Archivos:**
   El cliente despliega un menú que permite al usuario seleccionar un archivo existente en el proyecto o elegir un archivo desde su ordenador.

3. **Intercambio de Claves con Diffie-Hellman:**
   Cliente y servidor realizan un intercambio de claves utilizando el algoritmo de Diffie-Hellman para establecer una clave compartida de forma segura.

4. **Envío del Archivo Cifrado:**
   Utilizando la clave compartida y el algoritmo AES-256, el cliente cifra el archivo y lo envía de manera segura al servidor a través del canal de envío.

5. **Recepción y Descifrado del Archivo en el Servidor:**
   El servidor recibe el archivo cifrado, lo descifra utilizando el mismo algoritmo AES-256 en modo desencriptado y luego compara el hash del archivo original con el hash calculado a partir del archivo descifrado.

6. **Respuesta al Cliente:**
   El servidor envía el resultado de la comparación al cliente, indicando si el archivo llegó completo o con errores.

## Dificultades Encontradas

### 1. Problema con el Cierre de Sockets
- **Dificultad:** Cerrar el enlace de envío del cliente cerraba la comunicación con el servidor.
- **Solución:** Se optó por cerrar tanto el socket del servidor como del cliente, generando uno nuevo para continuar transmitiendo información.

### 2. Operador XOR en Lugar de Exponente
- **Dificultad:** Se utilizaba el operador ^ para elevar enteros, representando un XOR en lugar de un exponente.
- **Solución:** Se recurrió a los métodos de BigInteger para realizar la operación de forma adecuada.

### 3. Complejidad de las Librerías de Cifrado y Descifrado
- **Dificultad:** La complejidad interna de las librerías para cifrar y descifrar a veces dificultaba su uso.
- **Solución:** Se realizó un estudio más profundo de las funciones y métodos de las librerías, y se documentaron adecuadamente para comprender su funcionamiento.

## Conclusiones

En conclusión, el programa representa una aplicación integral de los conocimientos adquiridos en el curso de ciberseguridad. La capacidad de cifrar y descifrar archivos de manera efectiva resalta la importancia de la seguridad en las transmisiones de datos. El concepto de reconstruir la información original a partir de datos cifrados es fascinante y destaca la relevancia de la ciberseguridad. Aunque la implementación de una interfaz gráfica se quedó pendiente, se considera que el programa cumple con los objetivos planteados.