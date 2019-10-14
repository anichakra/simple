#!groovy
// Pipeline as code using Jenkinsfile for a microservice
// @author Anirban Chakraborty

node {
  // Maven Artifact Id and Version
  def ARTIFACT_ID = "simple-rest-service"
  def VERSION     = "0.0.1.BUILD-SNAPSHOT"
  // Sonar configuration attributes
  def SONAR_TOKEN = "0af30a17a1f3987a83773a9096ef1306957b5bd5"
  def SONAR_URL = "http://cloudnativelab-sonar-alb-1809467691.us-east-1.elb.amazonaws.com"    
  
  // Nexus URL
  def NEXUS_URL="http://cloudnativelab-nexus-alb-1228301333.us-east-1.elb.amazonaws.com"
   
  // AWS ECS attributes
  def AWS_ECS_CLUSTER_NAME  = "cloudnativelab-ecs-cluster"
  def AWS_ECS_TASK_COUNT    = 3 
  // ECS Service and Task Definition Name
  def AWS_ECS_SERVICE_NAME  =  ARTIFACT_ID
  def AWS_ECS_TASK_DEF_NAME =  ARTIFACT_ID + "-task"
  // Whether to use the updated task definition and make a new revision
  def UPDATE_AWS_ECS_TASKDEF_REV = true
          
  // ID of credentials in Jenkins as configured in Jenkins project
  def AWS_CREDENTIAL_ID = "aws_id"  
      
  // AWS attributes - might not be required to be changed often   
  def AWS_REGION  = "us-east-1"
  def AWS_ACCOUNT = "595233065713" 
  
  // Branch names of the repository for a multibranch pipeline 
  def DEV_BRANCH_NAME = "master"
  def UAT_BRANCH_NAME = "uat"
  def PRD_BRANCH_NAME = "prd"
  def SIT_BRANCH_NAME = "sit"
  
  //Provide/override all required values based on environment
  if  (env.BRANCH_NAME == UAT_BRANCH_NAME) {
    AWS_ECS_CLUSTER_NAME  = "cloudnativelab-ecs-cluster-uat"
  } else if (env.BRANCH_NAME == PRD_BRANCH_NAME) {
    AWS_ECS_CLUSTER_NAME  = "cloudnativelab-ecs-prd-cluster-prd"
  }
  
  ws("workspace/${env.JOB_NAME}/${env.BRANCH_NAME}") {
    try {      
      // Docker image details - might not be required to be changed often    
      def MAVEN_IMAGE  = "maven:3.6.2-amazoncorretto-11"
      def MAVEN_HOME   = "/var/maven"
      def MAVEN_VOLUME = "-v /home/ec2-user/.m2:" +MAVEN_HOME + " -e MAVEN_CONFIG="+MAVEN_HOME + "/.m2"
      
      
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
          sh("mvn clean test -Duser.home=" + MAVEN_HOME + " -Dnexus.url=" + NEXUS_URL)
        }
      }
   
      stage('Build') {
        println "########## Executing unit test cases ##########"
        docker.image(MAVEN_IMAGE).inside(MAVEN_VOLUME) {
          sh("mvn package -Duser.home=" + MAVEN_HOME + " -Dnexus.url=" + NEXUS_URL)
        }
      }
      
      stage('JAR Install') {
        if(env.BRANCH_NAME == DEV_BRANCH_NAME) {
          println "########## Installing jar files in local maven repository ##########"
          docker.image(MAVEN_IMAGE).inside(MAVEN_VOLUME) {
            sh("mvn install -Dmaven.test.skip=true -Duser.home=" + MAVEN_HOME + " -Dnexus.url=" + NEXUS_URL)
          }
        }
      }
                
      stage('JAR Upload') {
        if(env.BRANCH_NAME == DEV_BRANCH_NAME) {
          println "########## Deploying jar files to Nexus ##########"
          docker.image(MAVEN_IMAGE).inside(MAVEN_VOLUME) {
            sh("mvn deploy -Duser.home=" + MAVEN_HOME + " -Dnexus.url=" + NEXUS_URL)
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
      }          
    }
  }
}
