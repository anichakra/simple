#aws ecs update-service --cluster cloudnativelab-ecs-cluster --service simple-rest-service --force-new-deployment --region us-east-1

aws ecr get-login --region us-east-1 | xargs xargs