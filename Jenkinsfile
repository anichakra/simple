#!groovy
// Pipeline as code using Jenkinsfile for a microservice
// @author Anirban Chakraborty

node {
  // Maven Artifact Id and Version
  def ARTIFACT_ID = "simple-rest-service"
  def VERSION     = "0.0.1.BUILD-SNAPSHOT"
  // AWS ECS attributes
  def AWS_ECS_CLUSTER_NAME  = "cloudnativelab-ecs-cluster"
  def AWS_ECS_SERVICE_NAME  = "simple-rest-service" 
  def AWS_ECS_TASK_DEF_NAME = "simple-rest-service-task"
  def AWS_ECS_TASK_COUNT    = 4
      
  // AWS ECR Connection Token as configured in Jenkins ECR plugin
  def AWS_ECR_TOKEN = "5fe9919d-8fe5-42eb-9c4c-e38d3a7c3dbb"
      
  // ID of credentials in Jenkins as configured in Jenkins project
  def AWS_CREDENTIAL_ID = "aws_id"  
      
  // AWS attributes - might not be required to be changed often   
  def AWS_REGION  = "us-east-1"
  def AWS_ACCOUNT = "595233065713" 
 
  ws("workspace/${env.JOB_NAME}/${env.BRANCH_NAME}") {
    try {      
      // Docker image details - might not be required to be changed often    
      def MAVEN_IMAGE   = "maven:3-jdk-11"
      def AWS_CLI_IMAGE  = "mikesir87/aws-cli"
      def MAVEN_VOLUME     = "-v $HOME/.m2:/root/.m2"
      def AWS_CLI_VOLUME = "-v $HOME/.aws:/root/.aws"
      def awsCli = docker.build("aws-cli", "./aws")
      
      println "Pipeline started in workspace/" + env.JOB_NAME + "/" + env.BRANCH_NAME
      
      stage('SCM Checkout') {
        println "########## Checking out latest from git repo ##########"
        checkout scm
      }

      stage('Unit Testing') {
        println "########## Executing unit test cases ##########"
        docker.image(MAVEN_IMAGE).inside(MAVEN_VOLUME) {
          sh("mvn clean test")
        }
      }
  
  // Push reports to sonar
    
     stage('JAR Install') {
        println "########## Installing jar files in local maven repository ##########"
        docker.image(MAVEN_IMAGE).inside(MAVEN_VOLUME) {
          sh('mvn install')
        }
      }
      
      // Push jars to nexus
  
      stage('Docker Image Creation') {
        println "########## Creating docker images ##########"
        docker.image(MAVEN_IMAGE).inside(MAVEN_VOLUME) {
          sh('mvn docker:build') 
        }
      }
 
      stage('Docker Image Push'){
        println "########## Pushing docker images in ECR repository ##########"
        docker.withRegistry("https://" + AWS_ACCOUNT + ".dkr.ecr." + AWS_REGION + ".amazonaws.com", 
        "ecr:" + AWS_REGION + ":" + AWS_ECR_TOKEN) {
          docker.image(ARTIFACT_ID+":"+VERSION).push()
        }    
      }      
 
      stage('ECS Deploy') {
          
        println "########## Deploying services to ECS ##########"
        awsCli.inside(AWS_CLI_VOLUME) {
          withCredentials(
            [[
              $class: 'AmazonWebServicesCredentialsBinding',
              accessKeyVariable: 'AWS_ACCESS_KEY_ID',
              credentialsId: AWS_CREDENTIAL_ID,  
              secretKeyVariable: 'AWS_SECRET_ACCESS_KEY'
            ]]) {
            def taskDefile = "file://aws/task-definition-" + VERSION + ".json"
            sh("sed -e 's;%BUILD_TAG%;" + VERSION + ";g'                             \
                     aws/task-definition.json >                                      \
                     aws/task-definition-" + VERSION + ".json")
        
            def currentTaskDefCmd = "aws ecs describe-task-definition --task-definition " + AWS_ECS_TASK_DEF_NAME    \
                + " | egrep 'revision' | tr ',' ' ' | awk '{print \$2}'"
            def currTaskDef = sh (returnStdout: true, script: currentTaskDefCmd).trim()
          
            def taskListCmd = "aws ecs list-tasks  --cluster " + AWS_ECS_CLUSTER_NAME  \
                                               + " --family "  + AWS_ECS_TASK_DEF_NAME \
                                               + " --region "  + AWS_REGION            \
                                               + " --output text | awk '{print \$2}'"        
            
            def currentTasks = sh(returnStdout: true, script: taskListCmd).trim()
          
            println "Current Tasks: " + currentTasks  
            if(currTaskDef) {
                sh ("aws ecs update-service --cluster "         + AWS_ECS_CLUSTER_NAME  \
                                        + " --service "         + AWS_ECS_SERVICE_NAME  \
                                        + " --task-definition " + AWS_ECS_TASK_DEF_NAME \
                                        + ":"                   + currTaskDef           \
                                        + " --desired-count 0"                          \
                                        + " --region "          + AWS_REGION)          
              }
            
            while(currentTasks) {    
              def t = currentTasks.split("\n") 
              String[] taskArray = t as String[]
              println "No. of Task to stop: " + taskArray.length
              println "Stopping all the current tasks: " 
              
              for(i=0; i<taskArray.length; i++ ) {
                try {
                  def task = taskArray[ i ]        
                  println "Stopping Task: " + task
                  sh ("aws ecs stop-task --region " + AWS_REGION           \
                                     + " --cluster "+ AWS_ECS_CLUSTER_NAME \
                                     + " --task "   + task)
                                                    
                } catch (ee) {
                  println "Task cannot be stopped: " + ee.toString()
                }
              }
              // Register the new [TaskDefinition]
               sh("aws ecs register-task-definition --region " + AWS_REGION + " --family " + AWS_ECS_TASK_DEF_NAME + " --cli-input-json " + taskDefile)
             
                
              currentTasks = sh(returnStdout: true, script: taskListCmd).trim()
              println "Current Tasks: " + currentTasks  
              
              if (currentTasks) {
                t = currentTasks.split("\n") 
                taskArray = t as String[]
                println "Tasks Array: " + taskArray  
              }
            }
              // Get the last registered [TaskDefinition#revision]
           def taskRevisionCmd = "aws ecs describe-task-definition --region " + AWS_REGION + " --task-definition " + AWS_ECS_TASK_DEF_NAME     \
                                          +   " | egrep 'revision' | tr ',' ' ' | awk '{print \$2}'"
           def taskRevision = sh (returnStdout: true, script: taskRevisionCmd).trim()
        
            println "Updating ECS cluster"
            sh ("aws ecs update-service --cluster "         + AWS_ECS_CLUSTER_NAME  \
                                    + " --service "         + AWS_ECS_SERVICE_NAME  \
                                    + " --task-definition " + AWS_ECS_TASK_DEF_NAME \
                                    + ":"                   + taskRevision  \
                                    + " --desired-count "   + AWS_ECS_TASK_COUNT    \
                                    + " --region "          + AWS_REGION)
          }
        }
      }      
    } catch(e) {
      println "Err: Incremental Build failed with Error: " + e.toString()
      currentBuild.result = 'FAILED'
      throw e
    } finally  {
      stage('Cleanup') {
        println "Cleaning up"
        sh("docker image rm " + ARTIFACT_ID + ":" + VERSION)
        deleteDir()
      }          
    }
  }
}