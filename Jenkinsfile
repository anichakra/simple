node {
  checkout scm
  stage('Build Maven') {
    docker.image('maven:3-jdk-11').inside('-v $HOME/.m2:/root/.m2') {
      sh 'mvn clean install'
    }
  }
  stage('Build Docker Image') {
    docker.image('maven:3-jdk-11').inside('-v $HOME/.m2:/root/.m2') {
      sh 'mvn docker:build'    
    }
  }
  stage('Connect to AWS ECR') {
    sh 'aws ecr get-login --region us-east-1 | xargs xargs'   
  }
  stage('Push to AWS ECR'){
    docker.withRegistry('https://595233065713.dkr.ecr.us-east-1.amazonaws.com', 'ecr:us-east-1:5fe9919d-8fe5-42eb-9c4c-e38d3a7c3dbb') {
      docker.image('simple-rest-service:0.0.1.BUILD-SNAPSHOT').push()
    }    
  }
}
