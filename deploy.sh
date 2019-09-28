#aws ecs update-service --cluster cloudnativelab-ecs-cluster --service simple-rest-service --force-new-deployment --region us-east-1

aws ecr get-login --region us-east-1 | xargs xargs

stage("Checkout SCM") {
        checkout scm
      }
 
      stage('Maven Clean') {
        mavenImage.inside(m2Volume) {
          sh 'mvn clean'
        }
      }
  
      stage('Unit Test') {
        mavenImage.inside(m2Volume) {
          sh 'mvn test'
        }
      }
  
  // Push reports to sonar
  
      stage('Build JAR') {
        mavenImage.inside(m2Volume) {
          sh 'mvn package'
        }
      }
  
      stage('Install JAR') {
        mavenImage.inside(m2Volume) {
          sh 'mvn install'
        }
      }
  // Push jars to nexus
  
      stage('Build Docker Image') {
        mavenImage.inside(m2Volume) {
          sh 'mvn docker:build'    
        }
      }
  
  // check the image
  // run the image and health check
  // stop the container
  // remove container
  
     // stage('Connect to AWS ECR') {
       // sh 'aws ecr get-login --region us-east-1 | xargs xargs'   
     // }
  
      stage('Push Image to AWS ECR'){
        docker.withRegistry(ecrUrl, ecrToken) {
          docker.image(imageTag).push()
        }    
      }
