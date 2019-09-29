node {
  ws("workspace/${env.JOB_NAME}/${env.BRANCH_NAME}") {
    try {
    
      // Maven Artifact Id and Version
      def ARTIFACT_ID = "simple-rest-service"
      def VERSION     = "0.0.1.BUILD-SNAPSHOT"
      
      // AWS ECS attributes
      def AWS_ECS_TASK_DEF_NAME     = "simple-rest-service-task"
      def AWS_ECS_TASK_DEF_REVISION = 2
      def AWS_ECS_SERVICE_NAME      = "simple-rest-service"
      def AWS_ECS_CLUSTER_NAME      = "cloudnativelab-ecs-cluster"
      
      // AWS ECR Connection Token as configured in Jenkins ECR plugin
      def AWS_ECR_TOKEN = "5fe9919d-8fe5-42eb-9c4c-e38d3a7c3dbb"
      
      // ID of credentials in Jenkins as configured in Jenkins project
      def AWS_JENKINS_CREDENTIAL_ID = "aws_id"  
      
      // AWS attributes - might not be required to be changed often   
      def AWS_REGION  = "us-east-1"
      def AWS_ACCOUNT = "595233065713" 
      
      // Docker image details - might not be required to be changed often    
      def mavenImage   = "maven:3-jdk-11"
      def awsCliImage  = "mikesir87/aws-cli"
      def m2Volume     = "-v $HOME/.m2:/root/.m2"
      def awsCliVolume = "-v $HOME/.aws:/root/.aws"

      stage('Checkout from SCM') {
        echo "Checking out latest from git repo"
        checkout scm
      }

      stage('Execute Unit Test') {
        echo "Executing unit test cases"
        docker.image(mavenImage).inside(m2Volume) {
          sh 'mvn clean test'
        }
      }
  
  // Push reports to sonar
    
      stage('Install JAR') {
        echo "Installing jar file to local .m2 repository"
        docker.image(mavenImage).inside(m2Volume) {
          sh 'mvn install'
        }
      }
      
    // Push jars to nexus
  
      stage('Create Docker Image') {
        echo "Creating docker image"
        docker.image(mavenImage).inside(m2Volume) {
          sh 'mvn docker:build'    
        }
      }
  
  // check the image
  // run the image and health check
  // stop the container
  // remove container

      stage('ECR Docker Push'){
        echo "docker push to ECR repo"
        docker.withRegistry("https://${AWS_ACCOUNT}.dkr.ecr.${AWS_REGION}.amazonaws.com", "ecr:${AWS_REGION}:${AWS_ECR_TOKEN}") {
          docker.image(artifactId + ":" + version).push()
        }    
      }      
 
      stage('ECS Deploy') {
        docker.image(awsCliImage).inside(awsCliVolume) {               
          // Example AWS credentials
          withCredentials(
            [[
              $class: 'AmazonWebServicesCredentialsBinding',
              accessKeyVariable: 'AWS_ACCESS_KEY_ID',
              credentialsId: '${AWS_JENKINS_CREDENTIAL_ID}',  
              secretKeyVariable: 'AWS_SECRET_ACCESS_KEY'
            ]]) {
                
            echo "Listing ECS task for cluster ${clusterName} with family ${taskDefName}"
            def currentTask = sh (
              returnStdout: true,
              script:  "
                aws ecs list-tasks  --cluster ${clusterName}                                \
                                    --family ${taskDefName} --region ${region}              \
                                    --output text                                           \
                                    | egrep 'TASKARNS'                                      \
                                    | awk '{print \$2}'                                     \
              "
            ).trim()
            echo "Stopping all tasks for cluster ${clusterName} for service ${serviceName} with family ${taskDefName}:${taskDefRevision} "
            sh "aws ecs update-service --cluster ${clusterName}                             \
                                       --service ${serviceName}                             \
                                       --task-definition ${taskDefName}:${taskDefRevision}  \
                                       --desired-count 0                                    \
                                       --region ${region}" 
            
            echo "Current tasks: ${currentTask} "                                                    
            if (currentTask) {
              def splitted = currentTask.split('\n')
              for(i=0; i<splitted.length; i++ ) {
                def task = splitted[ i ]       
                try {   
                  sh "aws ecs stop-task --region ${region}                         
                                        --cluster ${clusterName} 
                                        --task ${task}"
                } catch (ee) {
                  echo 'Err: Task' + task +' cannot be stopped: ' + ee.toString() 
                }
              }
            }      
            
            echo "Stopping all tasks for cluster ${clusterName} for service ${serviceName} with family ${taskDefName}:${taskDefRevision} "
            sh "aws ecs update-service --cluster ${clusterName}                            \
                                       --service ${serviceName}                            \
                                       --task-definition ${taskDefName}:${taskDefRevision} \
                                       --desired-count ${desiredCount}                     \
                                       --region ${region}"
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
