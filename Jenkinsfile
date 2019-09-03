pipeline {
    agent none
    stages {
        stage('Back-end') {
            agent {
                docker { image 'maven:3-jdk-11' }
            }
            steps {
                sh 'mvn clean install'
            }
        }
    }
}