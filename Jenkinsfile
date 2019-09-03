pipeline {
    agent any
    
    options { disableConcurrentBuilds() }
    stages {
    
       stage('SCM Checkout') {
            // Clone repo
	     git branch: 'master', 
	     url: 'https://github.com/anichakra/simple'
   
        }
    
        stage('Build') {
            steps {
                sh '''
                 docker run -v ~/.m2:/root/.m2 -v "$PWD":/usr/src -w /usr/src maven:3-jdk-11 mvn clean install
                 docker run -v ~/.m2:/root/.m2 -v "$PWD":/usr/src -v /var/run/docker.sock:/var/run/docker.sock -w /usr/src maven:3-jdk-11 mvn docker:build
                '''
            }
            
        }
     }
        
       
}
