node {

  def mavenImage = "maven:3-jdk-11"
  def imageTag = "simple-rest-service:0.0.1.BUILD-SNAPSHOT"
  def ecrUrl = "https://595233065713.dkr.ecr.us-east-1.amazonaws.com"
  def ecrToken = "ecr:us-east-1:5fe9919d-8fe5-42eb-9c4c-e38d3a7c3dbb"

  
  stage("Checkout SCM") {
        checkout scm
  }

  stage('Maven Clean') {
  
    docker.image(mavenImage).inside('-v $HOME/.m2:/root/.m2') {
      sh 'mvn clean'
    }
  }
  
  stage('Unit Test') {
  
    docker.image(mavenImage).inside('-v $HOME/.m2:/root/.m2') {
      sh 'mvn test'
    }
  }
  
  // Push reports to sonar
  
  stage('Build JAR') {
    docker.image(mavenImage).inside('-v $HOME/.m2:/root/.m2') {
      sh 'mvn package'
    }
  }
  
  stage('Install JAR') {
    docker.image(mavenImage).inside('-v $HOME/.m2:/root/.m2') {
      sh 'mvn install'
    }
  }
  // Push jars to nexus
  
  stage('Build Docker Image') {
    docker.image(mavenImage).inside('-v $HOME/.m2:/root/.m2') {
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
  
  stage('Push Image to AWS ECR'){
    docker.withRegistry(ecrUrl, ecrToken) {
      docker.image(imageTag).push()
    }    
  }
  
  // remove the image
  // run ECS to get new image (canary release - rolling update)
  // run microservice automation script
  
}
