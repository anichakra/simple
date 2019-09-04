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
    docker.withRegistry('595233065713.dkr.ecr.us-east-1.amazonaws.com') {
      docker.image('cloudnative-lab/simple-rest-service:0.0.1.BUILD-SNAPSHOT').push()
    }    
  }
}
