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
        // Replace BUILD_TAG placeholder in the task-definition file -
        // with the remoteImageTag (imageTag-BUILD_NUMBER)
        sh  "                                                                     \
          sed -e  's;%BUILD_TAG%;${imageTag};g'                             \
                  task-definition.json >                                      \
                  task-definition-${imageTag}.json                      \
        "

        // Get current [TaskDefinition#revision-number]
        def currTaskDef = sh (
          returnStdout: true,
          script:  "                                                              \
            aws ecs describe-task-definition --task-definition ${taskFamily}      \
                                              | egrep 'revision'                  \
                                              | tr ',' ' '                        \
                                              | awk '{print \$2}'                 \
          "
        ).trim()

        def currentTask = sh (
          returnStdout: true,
          script:  "                                                              \
            aws ecs list-tasks --cluster ${clusterName}                           \
                               --family ${taskFamily}                             \
                               --output text                                      \
                                | egrep 'TASKARNS'                                \
                                | awk '{print \$2}'                               \
          "
        ).trim()

        /*
        / Scale down the service
        /   Note: specifiying desired-count of a task-definition in a service -
        /   should be fine for scaling down the service, and starting a new task,
        /   but due to the limited resources (Only one VM instance) is running
        /   there will be a problem where one container is already running/VM,
        /   and using a port(80/443), then when trying to update the service -
        /   with a new task, it will complaine as the port is already being used,
        /   as long as scaling down the service/starting new task run simulatenously
        /   and it is very likely that starting task will run before the scaling down service finish
        /   so.. we need to manually stop the task via aws ecs stop-task.
        */
        if(currTaskDef) {
          sh  "                                                                   \
            aws ecs update-service  --cluster ${clusterName}                      \
                                    --service ${serviceName}                      \
                                    --task-definition ${taskFamily}:${currTaskDef}\
                                    --desired-count 0                             \
          "
        }
        if (currentTask) {
          sh "aws ecs stop-task --cluster ${clusterName} --task ${currentTask}"
        }

        // Register the new [TaskDefinition]
        sh  "                                                                     \
          aws ecs register-task-definition  --family ${taskFamily}                \
                                            --cli-input-json ${taskDefile}        \
        "

        // Get the last registered [TaskDefinition#revision]
        def taskRevision = sh (
          returnStdout: true,
          script:  "                                                              \
            aws ecs describe-task-definition  --task-definition ${taskFamily}     \
                                              | egrep 'revision'                  \
                                              | tr ',' ' '                        \
                                              | awk '{print \$2}'                 \
          "
        ).trim()

        // ECS update service to use the newly registered [TaskDefinition#revision]
        //
        sh  "                                                                     \
          aws ecs update-service  --cluster ${clusterName}                        \
                                  --service ${serviceName}                        \
                                  --task-definition ${taskFamily}:${taskRevision} \
                                  --desired-count 1                               \
        "
      }

     
  // remove the image
  // run ECS to get new image (canary release - rolling update)
  // run microservice automation script
    } catch(e) {
      throw e
    }
  }
}
