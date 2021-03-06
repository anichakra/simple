#!groovy
// Pipeline as code using Jenkinsfile for a microservice
// @author Anirban Chakraborty

node {
  // Allowable branch names of the repository for a multibranch pipeline 
  def MASTER_BRANCH_NAME = "master"
  def DEVELOP_BRANCH_NAME = "develop"
  def UAT_BRANCH_NAME = "uat"
  
  // Maven Artifact Id and Version
  def ARTIFACT_ID = "simple-rest-service"
  def VERSION     = "0.0.1.BUILD-SNAPSHOT"
  // Sonar configuration attributes
  def SONAR_TOKEN = "0af30a17a1f3987a83773a9096ef1306957b5bd5"
  def SONAR_URL = "http://cloudnativelab-sonar-alb-1809467691.us-east-1.elb.amazonaws.com"    
  
  // Nexus URL
  def NEXUS_URL="	"
  
  
  // AWS ECS attributes (should change accordingly)
  def AWS_ECS_CLUSTER_NAME
  //Provide/override all required values based on environment
  if  (env.BRANCH_NAME == UAT_BRANCH_NAME) {
    AWS_ECS_CLUSTER_NAME  = "cloudnativelab-ecs-cluster-uat"
  } else if (env.BRANCH_NAME == DEVELOP_BRANCH_NAME) {
    AWS_ECS_CLUSTER_NAME  = "cloudnativelab-ecs-cluster"
  } else if (env.BRANCH_NAME == MASTER_BRANCH_NAME) {
    AWS_ECS_CLUSTER_NAME  = "cloudnativelab-ecs-cluster"
  } else {
    throw new Exception("Branch not considered for pipeline: + ${env.BRANCH_NAME} ")
  }
  // ECS Service 
  def AWS_ECS_SERVICE_NAME  =  ARTIFACT_ID
  // Whether to use the updated task definition and make a new revision
  def UPDATE_AWS_ECS_TASKDEF_REV = true
  def AWS_ECS_TASK_COUNT    = 3 
  
  // ID of credentials in Jenkins as configured in Jenkins project
  def AWS_CREDENTIAL_ID = "aws_new_key"  
      
  // AWS attributes - might not be required to be changed often   
  def AWS_REGION  = "us-east-1"
  def AWS_ACCOUNT = "595233065713" 
  
  ws("workspace/${env.JOB_NAME}/${env.BRANCH_NAME}") {
    try {      
      // Docker image details - might not be required to be changed often    
      def MAVEN_IMAGE  = "maven:3.6.2-amazoncorretto-11"
      def MAVEN_HOME   = "/var/maven"
      def MAVEN_VOLUME = "-v /home/ec2-user/.m2:" +MAVEN_HOME + "/.m2" + " -e MAVEN_CONFIG="+MAVEN_HOME + "/.m2"
      def MAVEN_ARG    = "-Dmaven.test.skip=true -Duser.home=" + MAVEN_HOME + " -Dnexus.url=" + NEXUS_URL
      
      sh('printenv | sort')
      println "Pipeline started in workspace/" + env.JOB_NAME + "/" + env.BRANCH_NAME
      stage('SCM Checkout') {
        println "########## Checking out latest from git repo ##########"
        checkout scm
      }

      stage('Unit Test') {
        println "########## Executing unit test cases ##########"
        docker.image(MAVEN_IMAGE).inside(MAVEN_VOLUME) {
          sh("mvn help:evaluate -Dexpression=settings.localRepository")
          sh("mvn clean test " + MAVEN_ARG)
        }
      }
   
      stage('Build') {
        println "########## Executing unit test cases ##########"
        docker.image(MAVEN_IMAGE).inside(MAVEN_VOLUME) {
          sh("mvn package " + MAVEN_ARG)
        }
        
      }
      
      stage('Sonar Analysis') {
        println "########## Executing sonar plugin ##########"
        docker.image(MAVEN_IMAGE).inside(MAVEN_VOLUME) {
          sh("mvn sonar:sonar " + MAVEN_ARG + " -Dsonar.projectKey=" + ARTIFACT_ID + " -Dsonar.host.url=" + SONAR_URL + " -Dsonar.login=" + SONAR_TOKEN)
        }
      }
      
      stage('JAR Install') {
        println "########## Installing jar files in local maven repository ##########"        
        docker.image(MAVEN_IMAGE).inside(MAVEN_VOLUME) {
          sh("mvn install " + MAVEN_ARG)
        }
      }
                
      stage('JAR Upload') {
        println "########## Deploying jar files to Nexus ##########"
        docker.image(MAVEN_IMAGE).inside(MAVEN_VOLUME) {
        //  sh("mvn deploy " + MAVEN_ARG)
        }
      }      
       
       
      stage('Docker Image Creation') {
        println "########## Creating docker images ##########"
        docker.image(MAVEN_IMAGE).inside(MAVEN_VOLUME) {
          sh('mvn docker:build') 
        }
      }
 
      stage('Docker Image ECR Push'){
        println "########## Pushing docker images in ECR repository ##########"
        docker.withRegistry("https://" + AWS_ACCOUNT + ".dkr.ecr." + AWS_REGION + ".amazonaws.com", 
                           "ecr:" + AWS_REGION + ":" + AWS_CREDENTIAL_ID) {
          docker.image(ARTIFACT_ID+":"+VERSION).push()
          sh("docker rmi " + ARTIFACT_ID + ":" + VERSION)    
        }
      }      
 
      stage('ECS Deploy') {
        println "########## Deploying services to ECS ##########"
        // major and minor version only
        def AWS_VERSION = VERSION;
        int firstDot = VERSION.indexOf('.')
        if (firstDot > 0) {
            int secondDot = VERSION.indexOf('.', VERSION.indexOf('.') + 1)
            if (secondDot > 0)
                 AWS_VERSION = VERSION.substring(0, secondDot).replace('.', '-')
        }
        def AWS_ECS_TASK_DEF_NAME =  ARTIFACT_ID + "-task-" +  AWS_VERSION
        
        def awsCli = docker.build("aws-cli", "./aws")      
        
        awsCli.inside("-v $HOME/.aws:/root/.aws") {
          withCredentials(
            [[
              $class: 'AmazonWebServicesCredentialsBinding',
              accessKeyVariable: 'AWS_ACCESS_KEY_ID',
              credentialsId: AWS_CREDENTIAL_ID,  
              secretKeyVariable: 'AWS_SECRET_ACCESS_KEY'
            ]]) {
         
            def currentTaskDefCmd = "aws ecs describe-task-definition --region" + AWS_REGION  + " --task-definition " + AWS_ECS_TASK_DEF_NAME \
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
                
              currentTasks = sh(returnStdout: true, script: taskListCmd).trim()
              println "Current Tasks: " + currentTasks  
              
              if (currentTasks) {
                t = currentTasks.split("\n") 
                taskArray = t as String[]
                println "Tasks Array: " + taskArray  
              }
            }
            
            if(UPDATE_AWS_ECS_TASKDEF_REV) {
              // Creating task definition file from workspace with a version
              sh("sed -e 's;%BUILD_TAG%;" + VERSION + ";g'            \
                  aws/task-definition.json >                          \
                  aws/task-definition-" + "tmp" + ".json")
              sh("sed -e 's;%AWS_ACCOUNT%;" + AWS_ACCOUNT + ";g'      \
                  aws/task-definition-" + "tmp" + ".json >            \
                  aws/task-definition-" + "tmp1" + ".json")
              sh("sed -e 's;%AWS_VERSION%;" + AWS_VERSION + ";g'      \
                  aws/task-definition-" + "tmp1" + ".json >           \
                  aws/task-definition-" + AWS_VERSION + ".json")
                              
              // Register the new [TaskDefinition]
              sh("aws ecs register-task-definition --region " + AWS_REGION \
                                             + " --family " + AWS_ECS_TASK_DEF_NAME \
                                             + " --cli-input-json file://aws/task-definition-" + AWS_VERSION + ".json") 
            }	
            
            // Get the last registered [TaskDefinition#revision]
            def taskRevisionCmd = "aws ecs describe-task-definition --region " + AWS_REGION \
                                                                + " --task-definition " + AWS_ECS_TASK_DEF_NAME \
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
        deleteDir()        
      }          
    }
  }
  
}



