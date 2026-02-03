# Usamos la imagen oficial de Tomcat
FROM tomcat:10.1-jdk17

# Eliminamos la app de ejemplo de Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Copiamos nuestro WAR a Tomcat
COPY target/api_rest.war /usr/local/tomcat/webapps/ROOT.war

# Exponemos el puerto
EXPOSE 8080

# Comando por defecto de Tomcat
CMD ["catalina.sh", "run"]