node {
  
  checkout scm
  stage('Clean') {
  
    docker.image('maven:3-jdk-11').inside('-v $HOME/.m2:/root/.m2') {
      sh 'mvn clean'
    }
  }
  stage('Unit Test') {
  
    docker.image('maven:3-jdk-11').inside('-v $HOME/.m2:/root/.m2') {
      sh 'mvn test'
    }
  }
  
  // sonar
  
  stage('Build') {
    docker.image('maven:3-jdk-11').inside('-v $HOME/.m2:/root/.m2') {
      sh 'mvn package'
    }
  }
  
  stage('Install') {
    docker.image('maven:3-jdk-11').inside('-v $HOME/.m2:/root/.m2') {
      sh 'mvn install'
    }
  }
  // nexus to push the jar
  
  stage('Build Docker Image') {
    docker.image('maven:3-jdk-11').inside('-v $HOME/.m2:/root/.m2') {
      sh 'mvn docker:build'    
    }
  }
  
  // check the image
  // run the image and health check
  // stop the container
  // remove container
  stage('Connect to AWS ECR') {
    sh 'aws ecr get-login --region us-east-1 | xargs xargs'   
  }
  stage('Push to AWS ECR'){
    docker.withRegistry('https://595233065713.dkr.ecr.us-east-1.amazonaws.com', 'ecr:us-east-1:5fe9919d-8fe5-42eb-9c4c-e38d3a7c3dbb') {
      docker.image('simple-rest-service:0.0.1.BUILD-SNAPSHOT').push()
    }    
  }
  
  // remove the image
  // run ECS to get new image (canary release - rolling update)
  // run microservice automation script
  
}
