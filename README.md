# ForocochesInviExtractor

Un simple tool para recibir notificaciones vía Telegram de los códigos de invitación a Forocoches subidos en Instagram.

El proyecto utiliza el web browser de Selenium, en modo headless (sin interfaz gráfica). Aquí puede encontrarse más información: https://www.seleniumhq.org


## Instalación

Para utilizar el programa necesitarás las librerías de Selenium que ya están incluidas en el proyecto, si prefieres utilizar Maven puedes instalarlas añadiendo la dependencia:

```        
        <dependency>				
            <groupId>org.seleniumhq.selenium</groupId>								
            <artifactId>selenium-java</artifactId>								
            <version>2.45.0</version>								
	</dependency>				
```

## Uso

Necesitarás un bot de Telegram que puedes crear con BotFather, el ID de tu chat de Telegram ya sea persona lo de grupo por el cual el bot enviará los mensajes y además una cuenta de Instagram.

Para ejecutar el programa...


```        
cd "path"
java -jar InviExtractor.jar
pause	
```

Nota: Es necesario tener instalado Google Chrome en su última versión para que el web browser de Selenium funcione correctamente.

## El límite de peticiones
Debe tenerse en cuenta que Instagram puede bloquear demasiadas peticiones de una misma dirección IP y arrojar un error 429: too many requests.

## Contribuir
Cualquier aportación es bien recibida... Para cambios estructurales mayores contactar antes conmigo.

## License
[MIT](https://choosealicense.com/licenses/mit/)
