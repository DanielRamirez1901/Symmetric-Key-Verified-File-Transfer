# <b> _*Transferencia segura de archivos con clase simetrica*_ </b>

    Se desarrollaron dos programas, un cliente y un servidor. El programa servidor escucha 
    por un puerto determinado, y espera la conexión del cliente. El cliente recibe un nombre de archivo 
    como parámetro. Una vez conectados cliente y servidor, el cliente negocia una clave de cifrado 
    con el servidor empleando el algoritmo Diffie-Hellman (convencional o de curvas elípticas), y luego 
    transfiere el archivo empleando el algoritmo AES con clave de 256 bits, usando la clave previamente 
    negociada. Al final del proceso el cliente calcula el hash SHA-256 del archivo que acaba de 
    transmitir, y lo envia al servidor. El servidor calcula el hash sobre el archivo recibido, 
    y lo compara con el hash recibido del cliente. Si son iguales, se indica que el archivo se 
    transfirió adecuadamente .



## <b> Documentación </b> 📄

- *[Informe](https://github.com/DanielRamirez1901/Symmetric-Key-Verified-File-Transfer/blob/main/Informe.md)* - Documentación del proyecto e informe final

## <b> Contruido con </b> 🛠

+ [Java](https://www.java.com/es/) - Lenguaje de programación usado.
+ [Intellij idea](https://www.jetbrains.com/idea/) - IDE usado.

## **Versionado** 📌

<div style="text-align: left">
    <a href="https://git-scm.com/" target="_blank"> <img src="https://raw.githubusercontent.com/devicons/devicon/2ae2a900d2f041da66e950e4d48052658d850630/icons/git/git-original.svg" height="60" width = "60" alt="Git"></a> 
    <a href="https://github.com/" target="_blank"> <img src="https://img.icons8.com/fluency-systems-filled/344/ffffff/github.png" height="60" width = "60" alt="GitHub"></a>
</div>


## <b> Hecho por: </b>
<b> 😊😊 Operation Group: 😊😊 </b>

+ [Daniel Ramirez Gomez](https://github.com/DanielRamirez1901 "Daniel Ramirez Gomez")

<br>

[![forthebadge](https://forthebadge.com/images/badges/made-with-java.svg)](https://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/built-with-love.svg)](https://forthebadge.com)



