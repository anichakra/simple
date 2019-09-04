pipeline {

    environment {

        IMAGE = 'cloudnative-lab/simple-rest-service:0.0.1.BUILD-SNAPSHOT'
        ECRURL = '595233065713.dkr.ecr.us-east-1.amazonaws.com'
    }
    
    agent none
    
    stages {
        stage('Build') {
            agent {
                docker { image 'maven:3-jdk-11' }
            }
            steps {
                sh 'mvn clean install'
            }
        }
        stage('Containerization') {
            agent {
                docker { image 'maven:3-jdk-11' }
            }
            steps {
                sh 'mvn docker:build'
            }
        }
        stage('Connect') {
              sh 'aws ecr get-login --region us-east-1 | xargs xargs'
 
        }
        
        stage('Publish')
        {
            steps
            {
                script
                {
                    
                    // Push the Docker image to ECR
                    docker.withRegistry(ECRURL)
                    {
                        docker.image(IMAGE).push()
                    }
                }
            }
        }
    }
}