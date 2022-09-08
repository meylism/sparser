mvn clean package
mvn jar:jar
mvn org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=target/sparser-1.0-SNAPSHOT.jar