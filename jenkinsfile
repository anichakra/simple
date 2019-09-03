pipeline {
    agent any
    
    options { disableConcurrentBuilds() }
    stages {
        stage('Permissions') {
            steps {
                sh 'chmod 775 *'
            }
        }
        stage('Build') {
            steps {
                sh '''
                 sudo docker run -v ~/.m2:/root/.m2 -v "$PWD":/usr/src -w /usr/src maven:3-jdk-11 mvn clean install
                 sudo docker run -v ~/.m2:/root/.m2 -v "$PWD":/usr/src -v /var/run/docker.sock:/var/run/docker.sock -w /usr/src maven:3-jdk-11 mvn docker:build
                '''
            }
            
        }
     }
        
       
}