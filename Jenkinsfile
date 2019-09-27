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
      def taskFamily    = "simple-rest-service-task"
      def clusterName   = "cloudnativelab-ecs-cluster"
      stage('Deploy') {
            steps {
                // Example AWS credentials
                withCredentials(
                [[
                    $class: 'AmazonWebServicesCredentialsBinding',
                    accessKeyVariable: 'AWS_ACCESS_KEY_ID',
                    credentialsId: 'AWS_ID',  // ID of credentials in Jenkins
                    secretKeyVariable: 'AWS_SECRET_ACCESS_KEY'
                ]]) {
                    echo "Listing contents of an S3 bucket."
                    sh "AWS_ACCESS_KEY_ID=${AWS_ACCESS_KEY_ID} \
                        AWS_SECRET_ACCESS_KEY=${AWS_SECRET_ACCESS_KEY} \
                        AWS_REGION=us-east-1 \
                        aws aws s3 ls"
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
