node {
  ws("workspace/${env.JOB_NAME}/${env.BRANCH_NAME}") {
    try {
      
      def mavenImage    = "maven:3-jdk-11"
      def m2Volume      = "-v $HOME/.m2:/root/.m2"
      def imageTag      = "simple-rest-service:0.0.1.BUILD-SNAPSHOT"
      def ecrUrl        = "https://595233065713.dkr.ecr.us-east-1.amazonaws.com"
      def ecrToken      = "ecr:us-east-1:5fe9919d-8fe5-42eb-9c4c-e38d3a7c3dbb"
      def taskDefile    = "file://task-definition-${imageTag}.json"
      def serviceName   = "simple-rest-service"
      def taskFamily    = "simple-rest-service-task"
      def clusterName   = "cloudnativelab-ecs-cluster"

      stage("Checkout SCM") {
        checkout scm
      }

      stage('Maven Clean') {
        docker.image(mavenImage).inside(m2Volume) {
          sh 'mvn clean'
        }
      }
  
      stage('Unit Test') {
        docker.image(mavenImage).inside(m2Volume) {
          sh 'mvn test'
        }
      }
  
  // Push reports to sonar
  
      stage('Build JAR') {
        docker.image(mavenImage).inside(m2Volume) {
          sh 'mvn package'
        }
      }
  
      stage('Install JAR') {
        docker.image(mavenImage).inside(m2Volume) {
          sh 'mvn install'
        }
      }
  // Push jars to nexus
  
      stage('Build Docker Image') {
        docker.image(mavenImage).inside(m2Volume) {
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
      
      stage("Deploy to AWS ECS") {
        sh 'aws ecs update-service --cluster cloudnativelab-ecs-cluster --service simple-rest-service --force-new-deployment --region us-east-1'
      }

     
  // remove the image
  // run ECS to get new image (canary release - rolling update)
  // run microservice automation script
    } catch(e) {
      echo 'Err: Incremental Build failed with Error: ' + e.toString()
      throw e
    }
  }
}
