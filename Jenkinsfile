node {
  ws("workspace/${env.JOB_NAME}/${env.BRANCH_NAME}") {
    try {
      
      def mavenImage    = docker.image("maven:3-jdk-11")
      def m2Volume      = "-v $HOME/.m2:/root/.m2"
      def imageTag      = "simple-rest-service:0.0.1.BUILD-SNAPSHOT"
      def ecrUrl        = "https://595233065713.dkr.ecr.us-east-1.amazonaws.com"
      def ecrToken      = "ecr:us-east-1:5fe9919d-8fe5-42eb-9c4c-e38d3a7c3dbb"
      def taskDefile    = "file://task-definition-${imageTag}.json"
      def serviceName   = "simple-rest-service"
      def taskDefName   = "simple-rest-service-task"
      def tasks         = 2
      def clusterName   = "cloudnativelab-ecs-cluster"
      
      stage('SCM') {
        checkout scm
      }

      stage('Unit Test') {
        mavenImage.inside(m2Volume) {
          sh 'mvn clean test'
        }
      }
  
  // Push reports to sonar
    
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
 

      stage('Build') {
        docker.image("mikesir87/aws-cli").inside("-v $HOME/.aws:/root/.aws") {
          //sh 'aws ecs update-service --cluster cloudnativelab-ecs-cluster --service simple-rest-service --task-definition simple-rest-service-task:2 --force-new-deployment --region us-east-1'                                                                               
         // sh 'aws s3 ls' 
                
                // Example AWS credentials
                withCredentials(
                [[
                    $class: 'AmazonWebServicesCredentialsBinding',
                    accessKeyVariable: 'AWS_ACCESS_KEY_ID',
                    credentialsId: 'aws_id',  // ID of credentials in Jenkins
                    secretKeyVariable: 'AWS_SECRET_ACCESS_KEY'
                ]]) {
                
                    echo "deploying"
                    def currentTask = sh (
          returnStdout: true,
          script:  "                                                             \
            aws ecs list-tasks  --cluster ${clusterName}                          \
                                --family ${taskDefName}  --region us-east-1                          \
                                --output text                                     \
                                | egrep 'TASKARNS'                                \
                                | awk '{print \$2}'                               \
          "
        ).trim()
                      sh "aws ecs update-service --cluster ${clusterName} --service ${serviceName} --task-definition ${taskDefName}:${tasks} --desired-count 0  --region us-east-1"
                     if (currentTask) {
          sh "aws ecs stop-task --cluster ${clusterName} --task ${currentTask} --region us-east-1"
        }         
                    sh "aws ecs update-service --cluster ${clusterName} --service ${serviceName} --task-definition ${taskDefName}:${tasks} --desired-count 4 --region us-east-1"
                }
            
        }
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
