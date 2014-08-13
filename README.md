spray-oauth2
============

spray-oauth2 provider

# работает через https
    +1. реализовать implicit sslContext и sslEngineProvider
    +2. в application.conf spray.can.server.ssl-encryption = on
    +3. создать сертификат 
        (keytool -genkey -keyalg RSA -alias selfsigned -keystore keystore.jks -storepass password -keysize 2048)


Две точки входа (все работает по https)

*   endpoint авторизации
*   endpoint токена

# endpoint авторизации

## /oauth/authorize
    Поступает запрос по этому адресу для возврата гранта клиенту. 
    владелец ресурса должен быть авторизован, если нет, то нужно провести авторизацию владельца ресурса.
     
