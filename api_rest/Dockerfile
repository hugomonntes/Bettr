FROM tomcat:10.1-jdk17

# Limpiamos Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Copiamos el WAR correcto
COPY target/api_rest.war /usr/local/tomcat/webapps/ROOT.war

# Exponemos el puerto
EXPOSE 8080
