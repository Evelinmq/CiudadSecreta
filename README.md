# Ciudad Secreta 🎇🔮<img width="361" height="691" alt="logo" src="https://github.com/user-attachments/assets/fe7b8ffe-b2b3-4b1e-bce0-80b3c3cb61a7" />
> **Proyecto Integrador - Desarrollo de Aplicaciones Móviles**
>
> **Semestre: 4 **Grupo: F
> **Fecha de entrega:** 11 de Diciembre

## ✨ Equipo de desarrollo

| Nombre Completo | Rol / Tareas Principales | Usuario GitHub |
| :--- | :--- | :--- |
| Aragón Márquez Andrea | UI Design, Repositorio, Sensores, Lógica | @Andreaara |
| Cruz Millán Getsemaní | Backend, Retrofit, UI Design, Repositorio | @GetsemaniCruz |
| Mojica Quintana Evelin Itzel | Sensores, Lógica, UI Design, Repositorio | @Evelinmq |

## 🎆 Descripción
**¿Qué hace la aplicación?**
Ciudad secreta es una aplicación que utiliza el sensor de GPS para acceder a diferentes puntos en el mapa, estos puntos contienen información sobre rumores o secretos de la 
zona del mundo marcada. Los usuarios pueden leer una cantidad de secretos ilimitada por día. Los secretos son publicados por otros usuarios de la apliacción.
Además de secretos, puedes encontrar datos curiosos y/o información importante de la zona. ¡Es perfecta si estás visitanto un lugar! ya que puedes obtener información publicada
por las personas de la localidad, ¡Información valiosa que no encontrarás en ninguna otra parte de internet!

**Objetivo:**
Demostrar la implementación de una arquitectura robusta en Android utilizando servicios web y hardware del dispositivo.

## 🎠 Stack Tecnológico y Características

Este proyecto ha sido desarrollado siguiendo estrictamente los lineamientos de la materia:

* **Lenguaje:** Kotlin 100%.
* **Interfaz de Usuario:** Jetpack Compose.
* **Arquitectura:** MVVM (Model-View-ViewModel).
* **Conectividad (API REST):** Retrofit.
    * **GET:** Obtiene los puntos marcados del mundo
    * **POST:** Publica un punto en el mundo junto a un secreto redactado por el usuario.
    * **UPDATE:** Actualiza la descripción del secreto anteriormente publicado.
    * **DELETE:** Borra  el punto marcado en el mapa junto a su descripción.
* **Sensor Integrado:** GPS
    * *Uso:* Accede al mapa, permite visualizar los puntos publicados en el mapa, así como también te permite colocar tus propios puntos.


## 🎈 Uso
_Cómo leer un secreto_
1. Busca un punto en el mapa, lo identificarás como una imagen con colores morados.
2. Presiona el punto encontrado.
3. !Ahora puedes leer el secreto!
4. Para salir del secreto, solo hace falta pulsar fuera del recuadro donde apareció el secreto.

_Cómo publicar un secreto_
1.  Manten presionada la zona del mapa donde deseas colocar el secreto.
2.  Se abrirá un recuadro emergente donde podrás escribir lo que desees.
3.  Da click en "publicar".
4.  ¡Felicidades! ¡Haz publicado un secreto!

_Eliminar un secreto_
1. Presiona el punto marcado en el mapa.
2. Presiona el boton de eliminar.
3. El secreto ha sido eliminado.
   Importante: solo puedes eliminar secretos que hayan sido publicados por tí

_Editar un secreto_
1. Presiona el punto marcado en el mapa.
2. Edita la información del secreto.
3. Guarda los cambios.
4. El secreto se actualizará automáticamente.
   Importante: solo puedes editar secretos que hayan sido publicados por tí

   ##  🎫 Capturas de Pantalla

[Coloca al menos 3 (investiga como agregarlas y se vean en GitHub)]

| Pantalla de Inicio | Operación CRUD | Uso del Sensor |
| :---: | :---: | :---: |
| ![Inicio](url_imagen) | ![CRUD](url_imagen) | ![Sensor](url_imagen) |

---

## 🎡 Instalación y Releases

El ejecutable firmado (.apk) se encuentra disponible en la sección de **Releases** de este repositorio.

[Liga correctamente tu link de releases en la siguiente sección]

1.  Ve a la sección "Releases" (o haz clic [aquí](link_a_tus_releases)).
2.  Descarga el archivo `.apk` de la última versión.
3.  Instálalo en tu dispositivo Android (asegúrate de permitir la instalación de orígenes desconocidos).
