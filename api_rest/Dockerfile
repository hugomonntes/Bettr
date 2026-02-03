# Usamos Tomcat con JDK 17
FROM tomcat:10.1-jdk17

# Borramos las aplicaciones por defecto de Tomcat para evitar conflictos
RUN rm -rf /usr/local/tomcat/webapps/*

# Copiamos el archivo .war desde la raíz de tu repo al directorio de Tomcat
# Lo renombramos a ROOT.war para que tu API responda en la URL principal (/)
COPY api_rest.war /usr/local/tomcat/webapps/ROOT.war

# Exponemos el puerto 8080 que es el que usa Tomcat por defecto
EXPOSE 8080

# Comando para iniciar Tomcat
CMD ["catalina.sh", "run"]