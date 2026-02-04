FROM tomcat:9-jdk17

# Limpiar apps por defecto
RUN rm -rf /usr/local/tomcat/webapps/*

# Copiar el WAR
COPY api_rest/target/api_rest.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]
